package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.ProcedureEquipmentLinkCreateDTO;
import com.tsukilc.idme.entity.ProcedureEquipmentLink;
import com.tsukilc.idme.service.ProcedureEquipmentLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedure-equipment-link")
public class ProcedureEquipmentLinkController {
    private final ProcedureEquipmentLinkService service;

    public ProcedureEquipmentLinkController(ProcedureEquipmentLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody ProcedureEquipmentLinkCreateDTO dto) {
        return ApiResponse.success(service.create(dto));
    }

    @GetMapping("/by-procedure/{procedureId}")
    public ApiResponse<List<ProcedureEquipmentLink>> getByProcedure(@PathVariable String procedureId) {
        return ApiResponse.success(service.getByProcedure(procedureId));
    }

    @GetMapping("/by-equipment/{equipmentId}")
    public ApiResponse<List<ProcedureEquipmentLink>> getByEquipment(@PathVariable String equipmentId) {
        return ApiResponse.success(service.getByEquipment(equipmentId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponse.success(null);
    }
}
