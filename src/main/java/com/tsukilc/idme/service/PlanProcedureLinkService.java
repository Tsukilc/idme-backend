package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.PlanProcedureLinkDao;
import com.tsukilc.idme.dto.PlanProcedureLinkCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.PlanProcedureLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PlanProcedureLinkService {
    private final PlanProcedureLinkDao dao;

    public PlanProcedureLinkService(PlanProcedureLinkDao dao) {
        this.dao = dao;
    }

    public String create(PlanProcedureLinkCreateDTO dto) {
        PlanProcedureLink entity = new PlanProcedureLink();
        entity.setPlan(new ObjectReference(dto.getPlan(), "WorkingPlan"));
        entity.setProcedure(new ObjectReference(dto.getProcedure(), "WorkingProcedure"));
        entity.setSequenceNo(dto.getSequenceNo());
        entity.setStandardDurationMin(dto.getStandardDurationMin());
        entity.setRequirement(dto.getRequirement());
        return dao.create(entity).getId();
    }

    public List<PlanProcedureLink> getByPlan(String planId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("plan", planId);
        return dao.findByCondition(condition, 1, 1000);
    }

    public void delete(String id) {
        dao.delete(id);
    }
}
