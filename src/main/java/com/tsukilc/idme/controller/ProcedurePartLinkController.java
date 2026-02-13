package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.ProcedurePartLinkCreateDTO;
import com.tsukilc.idme.entity.ProcedurePartLink;
import com.tsukilc.idme.service.ProcedurePartLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedure-part-link")
public class ProcedurePartLinkController {
    private final ProcedurePartLinkService service;

    public ProcedurePartLinkController(ProcedurePartLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody ProcedurePartLinkCreateDTO dto) {
        return ApiResponse.success(service.create(dto));
    }

    @GetMapping("/by-procedure/{procedureId}")
    public ApiResponse<List<ProcedurePartLink>> getByProcedure(@PathVariable String procedureId) {
        return ApiResponse.success(service.getByProcedure(procedureId));
    }

    @GetMapping("/by-part/{partId}")
    public ApiResponse<List<ProcedurePartLink>> getByPart(@PathVariable String partId) {
        return ApiResponse.success(service.getByPart(partId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponse.success(null);
    }
}
