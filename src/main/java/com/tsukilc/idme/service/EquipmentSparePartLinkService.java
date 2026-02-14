package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.EquipmentSparePartLinkDao;
import com.tsukilc.idme.dto.EquipmentSparePartLinkCreateDTO;
import com.tsukilc.idme.entity.EquipmentSparePartLink;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.EquipmentSparePartLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EquipmentSparePartLinkService {
    private final EquipmentSparePartLinkDao dao;

    public EquipmentSparePartLinkService(EquipmentSparePartLinkDao dao) {
        this.dao = dao;
    }

    /**
     * 创建设备-备件关联
     */
    public EquipmentSparePartLinkVO create(EquipmentSparePartLinkCreateDTO dto) {
        log.info("创建设备-备件关联: {}", dto);
        EquipmentSparePartLink entity = convertToEntity(dto);
        EquipmentSparePartLink created = dao.create(entity);
        return convertToVO(created);
    }

    /**
     * 更新设备-备件关联
     */
    public EquipmentSparePartLinkVO update(String id, EquipmentSparePartLinkCreateDTO dto) {
        log.info("更新设备-备件关联，ID: {}, DTO: {}", id, dto);

        EquipmentSparePartLink existing = dao.findById(id);
        if (existing == null) {
            throw new IdmeException("设备备件关联不存在: " + id);
        }

        updateEntityFromDTO(existing, dto);
        existing.setId(id);

        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);

        EquipmentSparePartLink updated = dao.update(existing);
        return convertToVO(updated);
    }

    /**
     * 查询详情
     */
    public EquipmentSparePartLinkVO getById(String id) {
        log.info("查询设备备件关联详情，ID: {}", id);
        EquipmentSparePartLink entity = dao.findById(id);
        if (entity == null) {
            throw new IdmeException("设备备件关联不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 分页查询列表
     */
    public List<EquipmentSparePartLinkVO> list(int pageNum, int pageSize) {
        log.info("分页查询设备-备件关联列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<EquipmentSparePartLink> entities = dao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 按设备查询
     */
    public List<EquipmentSparePartLinkVO> getByEquipment(String equipmentId) {
        log.info("按设备查询备件关联，equipmentId: {}", equipmentId);
        Map<String, Object> condition = new HashMap<>();
        condition.put("equipment", equipmentId);
        List<EquipmentSparePartLink> entities = dao.findByCondition(condition, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 按物料查询（查询该物料作为备件被哪些设备使用）
     */
    public List<EquipmentSparePartLinkVO> getByPart(String partId) {
        log.info("查询备件使用情况，物料ID: {}", partId);
        Map<String, Object> condition = new HashMap<>();
        condition.put("sparePart", partId);
        List<EquipmentSparePartLink> entities = dao.findByCondition(condition, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(String id) {
        log.info("删除设备-备件关联，ID: {}", id);
        dao.delete(id);
    }

    /**
     * DTO -> Entity
     */
    private EquipmentSparePartLink convertToEntity(EquipmentSparePartLinkCreateDTO dto) {
        EquipmentSparePartLink entity = new EquipmentSparePartLink();
        entity.setEquipment(new ObjectReference(dto.getEquipment(), "Equipment"));
        entity.setSparePart(new ObjectReference(dto.getSparePart(), "Part"));
        entity.setQuantity(dto.getQuantity());
        if (dto.getUnit() != null) {
            entity.setUnit(new ObjectReference(dto.getUnit(), "Unit"));
        }
        entity.setIsCritical(dto.getIsCritical());
        entity.setReplacementCycleDays(dto.getReplacementCycleDays());
        entity.setRemarks(dto.getRemarks());
        return entity;
    }

    /**
     * 从 DTO 更新 Entity
     */
    private void updateEntityFromDTO(EquipmentSparePartLink entity, EquipmentSparePartLinkCreateDTO dto) {
        if (dto.getEquipment() != null) {
            entity.setEquipment(new ObjectReference(dto.getEquipment(), "Equipment"));
        }
        if (dto.getSparePart() != null) {
            entity.setSparePart(new ObjectReference(dto.getSparePart(), "Part"));
        }
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getUnit() != null) {
            entity.setUnit(new ObjectReference(dto.getUnit(), "Unit"));
        }
        if (dto.getIsCritical() != null) {
            entity.setIsCritical(dto.getIsCritical());
        }
        if (dto.getReplacementCycleDays() != null) {
            entity.setReplacementCycleDays(dto.getReplacementCycleDays());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
    }

    /**
     * Entity -> VO
     */
    private EquipmentSparePartLinkVO convertToVO(EquipmentSparePartLink entity) {
        EquipmentSparePartLinkVO vo = new EquipmentSparePartLinkVO();
        vo.setId(entity.getId());

        if (entity.getEquipment() != null) {
            vo.setEquipment(entity.getEquipment().getId());
        }
        if (entity.getSparePart() != null) {
            vo.setSparePart(entity.getSparePart().getId());
        }

        if (entity.getUnit() != null) {
            vo.setUnit(entity.getUnit().getId());
        }

        vo.setQuantity(entity.getQuantity());
        vo.setReplacementCycleDays(entity.getReplacementCycleDays());
        vo.setIsCritical(entity.getIsCritical());
        vo.setRemarks(entity.getRemarks());

        return vo;
    }
}
