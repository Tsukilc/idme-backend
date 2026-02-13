package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.PlanProcedureLinkCreateDTO;
import com.tsukilc.idme.entity.PlanProcedureLink;
import com.tsukilc.idme.service.PlanProcedureLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan-procedure-link")
public class PlanProcedureLinkController {
    private final PlanProcedureLinkService service;

    public PlanProcedureLinkController(PlanProcedureLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody PlanProcedureLinkCreateDTO dto) {
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
}
