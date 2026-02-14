package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.PlanProcedureLinkDao;
import com.tsukilc.idme.dto.PlanProcedureLinkBatchDTO;
import com.tsukilc.idme.dto.PlanProcedureLinkCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.PlanProcedureLink;
import com.tsukilc.idme.exception.IdmeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    /**
     * 批量添加工序到工艺路线
     */
    public List<String> batchCreate(PlanProcedureLinkBatchDTO dto) {
        log.info("批量添加工序到工艺路线，planId: {}, 工序数量: {}", dto.getPlanId(), dto.getProcedures().size());

        List<String> createdIds = new ArrayList<>();
        for (PlanProcedureLinkBatchDTO.ProcedureItem item : dto.getProcedures()) {
            PlanProcedureLink entity = new PlanProcedureLink();
            entity.setPlan(new ObjectReference(dto.getPlanId(), "WorkingPlan"));
            entity.setProcedure(new ObjectReference(item.getProcedureId(), "WorkingProcedure"));
            entity.setSequenceNo(item.getSequenceNo());
            entity.setStandardDurationMin(item.getStandardDurationMin() != null ?
                java.math.BigDecimal.valueOf(item.getStandardDurationMin()) : null);
            entity.setRequirement(item.getRequirement());

            String id = dao.create(entity).getId();
            createdIds.add(id);
        }

        log.info("批量添加工序完成，共创建 {} 条记录", createdIds.size());
        return createdIds;
    }

    /**
     * 按 ID 查询
     */
    public PlanProcedureLink getById(String id) {
        log.info("查询工序关联详情，ID: {}", id);
        PlanProcedureLink entity = dao.findById(id);
        if (entity == null) {
            throw new IdmeException("工序关联不存在: " + id);
        }
        return entity;
    }

    /**
     * 更新顺序号
     */
    public void updateSequence(String id, Integer sequenceNo) {
        log.info("更新工序顺序，ID: {}, 新顺序号: {}", id, sequenceNo);

        PlanProcedureLink existing = dao.findById(id);
        if (existing == null) {
            throw new IdmeException("工序关联不存在: " + id);
        }

        existing.setSequenceNo(sequenceNo);
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
        log.info("工序顺序更新成功");
    }
}
