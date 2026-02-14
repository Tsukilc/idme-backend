package com.tsukilc.idme.service;

import com.tsukilc.idme.client.IdmeSdkClient;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.WorkingPlanDao;
import com.tsukilc.idme.dto.PlanProcedureItem;
import com.tsukilc.idme.dto.WorkingPlanCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.PlanProcedureLink;
import com.tsukilc.idme.entity.WorkingPlan;
import com.tsukilc.idme.vo.WorkingPlanVO;
import com.tsukilc.idme.vo.WorkingProcedureVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工艺路线服务层
 * 竞赛核心模块（10分）- VersionObject类型
 */
@Service
@Slf4j
public class WorkingPlanService {

    private final WorkingPlanDao workingPlanDao;

    @Autowired
    private IdmeSdkClient sdkClient;

    @Autowired
    private PlanProcedureLinkService planProcedureLinkService;

    @Autowired
    private WorkingProcedureService workingProcedureService;

    public WorkingPlanService(WorkingPlanDao workingPlanDao) {
        this.workingPlanDao = workingPlanDao;
    }

    /**
     * 创建工艺路线
     */
    public WorkingPlanVO create(WorkingPlanCreateDTO dto) {
        WorkingPlan entity = convertToEntity(dto);
        WorkingPlan created = workingPlanDao.create(entity);
        return convertToVO(created);
    }

    /**
     * 分页查询工艺路线
     */
    public PageResult<WorkingPlanVO> list(int pageNum, int pageSize) {
        List<WorkingPlan> list = workingPlanDao.findAll(pageNum, pageSize);
        List<WorkingPlanVO> vos = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<WorkingPlanVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 根据ID查询工艺路线详情
     */
    public WorkingPlanVO getById(String id) {
        WorkingPlan entity = workingPlanDao.findById(id);
        return convertToVO(entity);
    }

    /**
     * 更新工艺路线
     */
    public void update(String id, WorkingPlanCreateDTO dto) {
        WorkingPlan entity = convertToEntity(dto);
        entity.setId(id);
        workingPlanDao.update(entity);
    }

    /**
     * 删除工艺路线
     */
    public void delete(String id) {
        workingPlanDao.delete(id);
    }

    /**
     * 创建工艺新版本（版本对象特有功能）
     * 流程：checkout → checkin（创建新iteration）
     */
    public WorkingPlanVO createNewVersion(String id) {
        log.info("创建工艺新版本，源ID: {}", id);

        // 获取当前对象的masterId
        WorkingPlan current = workingPlanDao.findById(id);
        String masterId = current.getMaster() != null ? current.getMaster().getId() : id;

        // 1. Checkout（创建工作副本）
        WorkingPlan workingCopy = sdkClient.checkout("WorkingPlan", masterId, "BOTH", WorkingPlan.class);
        log.info("Checkout成功，工作副本ID: {}", workingCopy.getId());

        // 2. Checkin（保存为新版本）
        WorkingPlan newVersion = sdkClient.checkin("WorkingPlan", masterId, "", WorkingPlan.class);
        log.info("Checkin成功，新版本ID: {}, iteration: {}", newVersion.getId(), newVersion.getIteration());

        return convertToVO(newVersion);
    }

    /**
     * 查询工艺对应所有工序
     * 返回包含工序详细信息的列表
     */
    public List<PlanProcedureItem> getProcedures(String planId) {
        log.info("查询工艺工序列表，planId: {}", planId);

        // 1. 查询所有关联（PlanProcedureLink）
        List<PlanProcedureLink> links = planProcedureLinkService.getByPlan(planId);

        // 2. 转换为 PlanProcedureItem（包含工序详细信息）
        List<PlanProcedureItem> items = new ArrayList<>();
        for (PlanProcedureLink link : links) {
            PlanProcedureItem item = new PlanProcedureItem();

            // 关联信息
            item.setLinkId(link.getId());
            item.setSequence(link.getSequenceNo());

            // standardDurationMin字段是Map格式，提取value
            if (link.getStandardDurationMin() != null && link.getStandardDurationMin() instanceof java.util.Map) {
                Object valueObj = ((java.util.Map<?, ?>) link.getStandardDurationMin()).get("value");
                if (valueObj != null) {
                    item.setStandardDurationMin(new BigDecimal(valueObj.toString()));
                }
            }

            // 工序详细信息
            // link.getProcedure()或link.getTarget()都可以获取工序引用
            ObjectReference procedureRef = link.getProcedure() != null ? link.getProcedure() : link.getTarget();
            if (procedureRef != null && procedureRef.getId() != null) {
                WorkingProcedureVO procedure = workingProcedureService.getById(procedureRef.getId());
                item.setProcedureId(procedure.getId());
                item.setProcedureCode(procedure.getProcedureCode());
                item.setProcedureName(procedure.getProcedureName());
                item.setSteps(procedure.getSteps());
                item.setStatus(procedure.getStatus());
                item.setMainProductionEquipment(procedure.getMainProductionEquipment());
                item.setMainInspectionEquipment(procedure.getMainInspectionEquipment());
                item.setOperatorRef(procedure.getOperatorRef());
                item.setRemarks(procedure.getRemarks());
            }

            items.add(item);
        }

        log.info("查询完成，共 {} 个工序", items.size());
        return items;
    }

    /**
     * DTO -> Entity
     */
    private WorkingPlan convertToEntity(WorkingPlanCreateDTO dto) {
        WorkingPlan entity = new WorkingPlan();
        entity.setPlanCode(dto.getPlanCode());
        entity.setPlanName(dto.getPlanName());
        entity.setBusinessVersion(dto.getBusinessVersion());
        
        // 处理 productPart 引用
        if (StringUtils.hasText(dto.getProductPart())) {
            entity.setProductPart(new ObjectReference(dto.getProductPart(), "Part"));
        }
        
        entity.setDescription(dto.getDescription());
        entity.setOperatorUser(dto.getOperatorUser());
        
        // 处理 operatorRef 引用
        if (StringUtils.hasText(dto.getOperatorRef())) {
            entity.setOperatorRef(new ObjectReference(dto.getOperatorRef(), "Employee"));
        }

        // 处理 operateTime（LocalDate -> LocalDateTime）
        if (dto.getOperateTime() != null) {
            entity.setOperateTime(dto.getOperateTime().atStartOfDay());
        }
        entity.setEquipmentUsage(dto.getEquipmentUsage());
        entity.setStatus(dto.getStatus());
        entity.setRemarks(dto.getRemarks());

        // VersionObject 必需字段：master 和 branch
        // 首次创建时传空对象，SDK会自动创建主对象和分支对象
        ObjectReference master = new ObjectReference();
        ObjectReference branch = new ObjectReference();
        entity.setMaster(master);
        entity.setBranch(branch);

        return entity;
    }

    /**
     * Entity -> VO
     */
    private WorkingPlanVO convertToVO(WorkingPlan entity) {
        WorkingPlanVO vo = new WorkingPlanVO();
        vo.setId(entity.getId());
        
        // 处理 master 引用
        if (entity.getMaster() != null) {
            vo.setMasterId(entity.getMaster().getId());
        }
        
        vo.setPlanCode(entity.getPlanCode());
        vo.setPlanName(entity.getPlanName());
        vo.setBusinessVersion(entity.getBusinessVersion());
        vo.setVersion(entity.getVersion());
        
        // 处理 productPart 引用
        if (entity.getProductPart() != null) {
            vo.setProductPart(entity.getProductPart().getId());
            vo.setProductPartName(entity.getProductPart().getDisplayName() != null 
                    ? entity.getProductPart().getDisplayName() 
                    : entity.getProductPart().getName());
        }
        
        vo.setDescription(entity.getDescription());
        vo.setOperatorUser(entity.getOperatorUser());
        
        // 处理 operatorRef 引用
        if (entity.getOperatorRef() != null) {
            vo.setOperatorRef(entity.getOperatorRef().getId());
            vo.setOperatorName(entity.getOperatorRef().getDisplayName() != null 
                    ? entity.getOperatorRef().getDisplayName() 
                    : entity.getOperatorRef().getName());
        }
        
        vo.setOperateTime(entity.getOperateTime());
        vo.setEquipmentUsage(entity.getEquipmentUsage());
        vo.setStatus(entity.getStatus());
        vo.setRemarks(entity.getRemarks());
        vo.setLatest(entity.getLatest());
        vo.setWorkingCopy(entity.getWorkingCopy());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());
        
        return vo;
    }
}
