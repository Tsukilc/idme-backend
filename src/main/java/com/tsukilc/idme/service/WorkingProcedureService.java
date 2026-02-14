package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.WorkingProcedureDao;
import com.tsukilc.idme.dto.WorkingProcedureBatchCreateDTO;
import com.tsukilc.idme.dto.WorkingProcedureCreateDTO;
import com.tsukilc.idme.dto.WorkingProcedureStatusUpdateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.WorkingProcedure;
import com.tsukilc.idme.vo.WorkingProcedureVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工序服务层
 * 竞赛核心模块（5分）- 支持批量创建和状态更新
 */
@Service
@Slf4j
public class WorkingProcedureService {

    private final WorkingProcedureDao workingProcedureDao;

    public WorkingProcedureService(WorkingProcedureDao workingProcedureDao) {
        this.workingProcedureDao = workingProcedureDao;
    }

    /**
     * 创建工序
     */
    public String create(WorkingProcedureCreateDTO dto) {
        WorkingProcedure entity = convertToEntity(dto);
        WorkingProcedure created = workingProcedureDao.create(entity);
        return created.getId();
    }

    /**
     * 批量创建工序（竞赛要求）
     */
    public List<String> batchCreate(WorkingProcedureBatchCreateDTO dto) {
        log.info("批量创建工序，数量: {}", dto.getProcedures().size());
        List<String> ids = new ArrayList<>();
        
        for (WorkingProcedureCreateDTO procedureDto : dto.getProcedures()) {
            String id = create(procedureDto);
            ids.add(id);
        }
        
        log.info("批量创建完成，成功创建 {} 个工序", ids.size());
        return ids;
    }

    /**
     * 分页查询工序
     */
    public PageResult<WorkingProcedureVO> list(int pageNum, int pageSize) {
        List<WorkingProcedure> list = workingProcedureDao.findAll(pageNum, pageSize);
        List<WorkingProcedureVO> vos = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<WorkingProcedureVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 根据ID查询工序详情
     */
    public WorkingProcedureVO getById(String id) {
        WorkingProcedure entity = workingProcedureDao.findById(id);
        return convertToVO(entity);
    }

    /**
     * 更新工序
     */
    public void update(String id, WorkingProcedureCreateDTO dto) {
        WorkingProcedure entity = convertToEntity(dto);
        entity.setId(id);
        workingProcedureDao.update(entity);
    }

    /**
     * 更新工序状态（竞赛要求）
     */
    public void updateStatus(String id, WorkingProcedureStatusUpdateDTO dto) {
        log.info("更新工序状态，ID: {}, 新状态: {}", id, dto.getStatus());
        
        // 查询现有工序
        WorkingProcedure entity = workingProcedureDao.findById(id);
        
        // 更新状态相关字段
        entity.setStatus(dto.getStatus());
        if (dto.getActualStart() != null) {
            entity.setStartTime(dto.getActualStart());
        }
        if (dto.getActualEnd() != null) {
            entity.setEndTime(dto.getActualEnd());
        }
        
        workingProcedureDao.update(entity);
        log.info("工序状态更新完成");
    }

    /**
     * 删除工序
     */
    public void delete(String id) {
        workingProcedureDao.delete(id);
    }

    /**
     * DTO -> Entity
     */
    private WorkingProcedure convertToEntity(WorkingProcedureCreateDTO dto) {
        WorkingProcedure entity = new WorkingProcedure();
        entity.setProcedureCode(dto.getProcedureCode());
        entity.setProcedureName(dto.getProcedureName());
        entity.setSteps(dto.getSteps());
        
        // 处理 mainProductionEquipment 引用
        if (StringUtils.hasText(dto.getMainProductionEquipment())) {
            entity.setMainProductionEquipment(
                new ObjectReference(dto.getMainProductionEquipment(), "Equipment"));
        }
        
        // 处理 mainInspectionEquipment 引用
        if (StringUtils.hasText(dto.getMainInspectionEquipment())) {
            entity.setMainInspectionEquipment(
                new ObjectReference(dto.getMainInspectionEquipment(), "Equipment"));
        }
        
        entity.setOperatorUser(dto.getOperatorUser());
        
        // 处理 operatorRef 引用
        if (StringUtils.hasText(dto.getOperatorRef())) {
            entity.setOperatorRef(new ObjectReference(dto.getOperatorRef(), "Employee"));
        }
        
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setStatus(dto.getStatus());
        entity.setRemarks(dto.getRemarks());
        
        return entity;
    }

    /**
     * Entity -> VO
     */
    private WorkingProcedureVO convertToVO(WorkingProcedure entity) {
        WorkingProcedureVO vo = new WorkingProcedureVO();
        vo.setId(entity.getId());
        vo.setProcedureCode(entity.getProcedureCode());
        vo.setProcedureName(entity.getProcedureName());
        vo.setSteps(entity.getSteps());

        // 处理 mainProductionEquipment 引用（只保存ID）
        if (entity.getMainProductionEquipment() != null) {
            vo.setMainProductionEquipment(entity.getMainProductionEquipment().getId());
        }

        // 处理 mainInspectionEquipment 引用（只保存ID）
        if (entity.getMainInspectionEquipment() != null) {
            vo.setMainInspectionEquipment(entity.getMainInspectionEquipment().getId());
        }

        vo.setOperatorUser(entity.getOperatorUser());

        // 处理 operatorRef 引用（只保存ID）
        if (entity.getOperatorRef() != null) {
            vo.setOperatorRef(entity.getOperatorRef().getId());
        }

        vo.setStartTime(entity.getStartTime());
        vo.setEndTime(entity.getEndTime());
        vo.setStatus(convertEnumField(entity.getStatus()));
        vo.setRemarks(entity.getRemarks());

        return vo;
    }

    /**
     * 转换枚举字段（处理SDK返回的Map结构）
     * SDK返回：{code:"XXX", cnName:"XXX", enName:"XXX", alias:"XXX"}
     * 期望输出：String (enName值)
     */
    private String convertEnumField(Object sdkEnum) {
        if (sdkEnum == null) {
            return null;
        }

        // SDK返回的是Map类型，提取enName字段
        if (sdkEnum instanceof java.util.Map) {
            Object enName = ((java.util.Map<?, ?>) sdkEnum).get("enName");
            if (enName != null) {
                return enName.toString();
            }
        }

        // 否则直接转换为字符串
        return sdkEnum.toString();
    }
}
