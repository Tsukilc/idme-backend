package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.WorkingPlanDao;
import com.tsukilc.idme.dto.WorkingPlanCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.WorkingPlan;
import com.tsukilc.idme.vo.WorkingPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public WorkingPlanService(WorkingPlanDao workingPlanDao) {
        this.workingPlanDao = workingPlanDao;
    }

    /**
     * 创建工艺路线
     */
    public String create(WorkingPlanCreateDTO dto) {
        WorkingPlan entity = convertToEntity(dto);
        WorkingPlan created = workingPlanDao.create(entity);
        return created.getId();
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
        
        entity.setOperateTime(dto.getOperateTime());
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
