package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.PlanProcedureLinkBatchDTO;
import com.tsukilc.idme.dto.PlanProcedureLinkCreateDTO;
import com.tsukilc.idme.dto.SequenceUpdateDTO;
import com.tsukilc.idme.entity.PlanProcedureLink;
import com.tsukilc.idme.service.PlanProcedureLinkService;
import com.tsukilc.idme.vo.PlanProcedureLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan-procedure-link")
@Slf4j
public class PlanProcedureLinkController {
    private final PlanProcedureLinkService service;

    public PlanProcedureLinkController(PlanProcedureLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<PlanProcedureLinkVO> create(@Validated @RequestBody PlanProcedureLinkCreateDTO dto) {
        return ApiResponse.success(service.create(dto));
    }

    @GetMapping("/by-plan/{planId}")
    public ApiResponse<List<PlanProcedureLink>> getByPlan(@PathVariable String planId) {
        return ApiResponse.success(service.getByPlan(planId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponse.success(null);
    }

    /**
     * 批量添加工序到工艺路线
     * 对应：POST /api/plan-procedure-link/batch
     */
    @PostMapping("/batch")
    public ApiResponse<List<String>> batchCreate(@Validated @RequestBody PlanProcedureLinkBatchDTO dto) {
        log.info("批量添加工序，planId: {}", dto.getPlanId());
        List<String> ids = service.batchCreate(dto);
        return ApiResponse.success(ids);
    }

    /**
     * 按 ID 查询工序关联
     * 对应：GET /api/plan-procedure-link/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<PlanProcedureLink> getById(@PathVariable String id) {
        log.info("查询工序关联，ID: {}", id);
        PlanProcedureLink link = service.getById(id);
        return ApiResponse.success(link);
    }

    /**
     * 更新工序顺序
     * 对应：PUT /api/plan-procedure-link/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> updateSequence(
            @PathVariable String id,
            @Validated @RequestBody SequenceUpdateDTO dto
    ) {
        log.info("更新工序顺序，ID: {}, 新顺序: {}", id, dto.getSequenceNo());
        service.updateSequence(id, dto.getSequenceNo());
        return ApiResponse.success(null);
    }
}
