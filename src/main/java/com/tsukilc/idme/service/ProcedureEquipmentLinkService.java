package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.ProcedureEquipmentLinkDao;
import com.tsukilc.idme.dto.ProcedureEquipmentLinkCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.ProcedureEquipmentLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProcedureEquipmentLinkService {
    private final ProcedureEquipmentLinkDao dao;

    public ProcedureEquipmentLinkService(ProcedureEquipmentLinkDao dao) {
        this.dao = dao;
    }

    public String create(ProcedureEquipmentLinkCreateDTO dto) {
        ProcedureEquipmentLink entity = new ProcedureEquipmentLink();
        entity.setProcedure(new ObjectReference(dto.getProcedure(), "WorkingProcedure"));
        entity.setEquipment1(new ObjectReference(dto.getEquipment1(), "Equipment"));
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
}
