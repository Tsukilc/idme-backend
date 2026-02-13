package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.EquipmentSparePartLinkDao;
import com.tsukilc.idme.dto.EquipmentSparePartLinkCreateDTO;
import com.tsukilc.idme.entity.EquipmentSparePartLink;
import com.tsukilc.idme.entity.ObjectReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EquipmentSparePartLinkService {
    private final EquipmentSparePartLinkDao dao;

    public EquipmentSparePartLinkService(EquipmentSparePartLinkDao dao) {
        this.dao = dao;
    }

    public String create(EquipmentSparePartLinkCreateDTO dto) {
        EquipmentSparePartLink entity = new EquipmentSparePartLink();
        entity.setEquipment(new ObjectReference(dto.getEquipment(), "Equipment"));
        entity.setSparePart(new ObjectReference(dto.getSparePart(), "Part"));
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());
        entity.setIsCritical(dto.getIsCritical());
        entity.setReplacementCycleDays(dto.getReplacementCycleDays());
        entity.setRemarks(dto.getRemarks());
        return dao.create(entity).getId();
    }

    public List<EquipmentSparePartLink> getByEquipment(String equipmentId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("equipment", equipmentId);
        return dao.findByCondition(condition, 1, 1000);
    }

    public void delete(String id) {
        dao.delete(id);
    }
}
