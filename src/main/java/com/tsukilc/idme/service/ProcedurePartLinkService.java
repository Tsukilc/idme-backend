package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.ProcedurePartLinkDao;
import com.tsukilc.idme.dto.ProcedurePartLinkCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.ProcedurePartLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProcedurePartLinkService {
    private final ProcedurePartLinkDao dao;

    public ProcedurePartLinkService(ProcedurePartLinkDao dao) {
        this.dao = dao;
    }

    public String create(ProcedurePartLinkCreateDTO dto) {
        ProcedurePartLink entity = new ProcedurePartLink();
        entity.setProcedure(new ObjectReference(dto.getProcedure(), "WorkingProcedure"));
        entity.setPart1(new ObjectReference(dto.getPart1(), "Part"));
        entity.setRole(dto.getRole());
        entity.setQuantity(dto.getQuantity());
        entity.setUom(dto.getUom());
        entity.setIsMandatory(dto.getIsMandatory());
        entity.setRemarks(dto.getRemarks());
        return dao.create(entity).getId();
    }

    public List<ProcedurePartLink> getByProcedure(String procedureId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("procedure", procedureId);
        return dao.findByCondition(condition, 1, 1000);
    }

    public List<ProcedurePartLink> getByPart(String partId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("part1", partId);
        return dao.findByCondition(condition, 1, 1000);
    }

    public void delete(String id) {
        dao.delete(id);
    }
}
