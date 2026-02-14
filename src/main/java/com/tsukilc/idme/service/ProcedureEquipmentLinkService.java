package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.ProcedureEquipmentLinkDao;
import com.tsukilc.idme.dto.ProcedureEquipmentLinkCreateDTO;
import com.tsukilc.idme.dto.ProcedureStatusUpdateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.ProcedureEquipmentLink;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.ProcedureEquipmentLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcedureEquipmentLinkService {
    private final ProcedureEquipmentLinkDao dao;

    public ProcedureEquipmentLinkService(ProcedureEquipmentLinkDao dao) {
        this.dao = dao;
    }

    public String create(ProcedureEquipmentLinkCreateDTO dto) {
        ProcedureEquipmentLink entity = new ProcedureEquipmentLink();
        // SDK同时需要source/target和procedure/equipment1
        ObjectReference procedureRef = new ObjectReference(dto.getProcedure(), "WorkingProcedure");
        ObjectReference equipmentRef = new ObjectReference(dto.getEquipment1(), "Equipment");
        entity.setSource(procedureRef);
        entity.setTarget(equipmentRef);
        entity.setProcedure(procedureRef);
        entity.setEquipment1(equipmentRef);
        entity.setRole(dto.getRole());
        entity.setPlannedStart(dto.getPlannedStart());
        entity.setPlannedEnd(dto.getPlannedEnd());
        entity.setActualStart(dto.getActualStart());
        entity.setActualEnd(dto.getActualEnd());
        entity.setRemarks(dto.getRemarks());
        return dao.create(entity).getId();
    }

    public List<ProcedureEquipmentLink> getByProcedure(String procedureId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("procedure", procedureId);
        return dao.findByCondition(condition, 1, 1000);
    }

    public List<ProcedureEquipmentLink> getByEquipment(String equipmentId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("equipment1", equipmentId);
        return dao.findByCondition(condition, 1, 1000);
    }

    public void delete(String id) {
        dao.delete(id);
    }

    /**
     * 分页查询
     */
    public PageResult<ProcedureEquipmentLinkVO> list(int pageNum, int pageSize) {
        List<ProcedureEquipmentLink> list = dao.findAll(pageNum, pageSize);
        List<ProcedureEquipmentLinkVO> vos = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<ProcedureEquipmentLinkVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 按 ID 查询
     */
    public ProcedureEquipmentLinkVO getById(String id) {
        log.info("查询工序设备关联详情，ID: {}", id);
        ProcedureEquipmentLink entity = dao.findById(id);
        if (entity == null) {
            throw new IdmeException("工序设备关联不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 更新
     */
    public void update(String id, ProcedureEquipmentLinkCreateDTO dto) {
        ProcedureEquipmentLink existing = dao.findById(id);
        if (existing == null) {
            throw new IdmeException("工序设备关联不存在: " + id);
        }

        // SDK同时需要source/target和procedure/equipment1
        ObjectReference procedureRef = new ObjectReference(dto.getProcedure(), "WorkingProcedure");
        ObjectReference equipmentRef = new ObjectReference(dto.getEquipment1(), "Equipment");
        existing.setSource(procedureRef);
        existing.setTarget(equipmentRef);
        existing.setProcedure(procedureRef);
        existing.setEquipment1(equipmentRef);
        existing.setRole(dto.getRole());
        existing.setPlannedStart(dto.getPlannedStart());
        existing.setPlannedEnd(dto.getPlannedEnd());
        existing.setActualStart(dto.getActualStart());
        existing.setActualEnd(dto.getActualEnd());
        existing.setRemarks(dto.getRemarks());
        existing.setId(id);

        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);

        dao.update(existing);
    }

    /**
     * 更新实际开始/结束时间
     */
    public void updateActualTime(String id, ProcedureStatusUpdateDTO dto) {
        log.info("更新工序实际时间，ID: {}", id);

        ProcedureEquipmentLink existing = dao.findById(id);
        if (existing == null) {
            throw new IdmeException("工序设备关联不存在: " + id);
        }

        if (dto.getActualStart() != null) {
            existing.setActualStart(dto.getActualStart());
        }
        if (dto.getActualEnd() != null) {
            existing.setActualEnd(dto.getActualEnd());
        }
        existing.setId(id);

        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);

        dao.update(existing);
        log.info("工序实际时间更新成功");
    }

    /**
     * Entity -> VO
     */
    private ProcedureEquipmentLinkVO convertToVO(ProcedureEquipmentLink entity) {
        ProcedureEquipmentLinkVO vo = new ProcedureEquipmentLinkVO();
        vo.setId(entity.getId());

        // SDK返回source/target，优先使用这些字段
        ObjectReference procedureRef = entity.getSource() != null ? entity.getSource() : entity.getProcedure();
        ObjectReference equipmentRef = entity.getTarget() != null ? entity.getTarget() : entity.getEquipment1();

        // 处理 procedure 引用
        if (procedureRef != null) {
            vo.setProcedure(procedureRef.getId());
            vo.setProcedureName(procedureRef.getDisplayName() != null
                    ? procedureRef.getDisplayName()
                    : procedureRef.getName());
        }

        // 处理 equipment 引用
        if (equipmentRef != null) {
            vo.setEquipment1(equipmentRef.getId());
            vo.setEquipmentName(equipmentRef.getDisplayName() != null
                    ? equipmentRef.getDisplayName()
                    : equipmentRef.getName());
        }

        vo.setRole(convertEnumField(entity.getRole()));
        vo.setPlannedStart(entity.getPlannedStart());
        vo.setPlannedEnd(entity.getPlannedEnd());
        vo.setActualStart(entity.getActualStart());
        vo.setActualEnd(entity.getActualEnd());
        vo.setRemarks(entity.getRemarks());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());

        return vo;
    }

    /**
     * 转换枚举字段（处理SDK返回的Map结构）
     */
    private String convertEnumField(Object sdkEnum) {
        if (sdkEnum == null) {
            return null;
        }

        if (sdkEnum instanceof java.util.Map) {
            Object enName = ((java.util.Map<?, ?>) sdkEnum).get("enName");
            if (enName != null) {
                return enName.toString();
            }
        }

        return sdkEnum.toString();
    }
}
