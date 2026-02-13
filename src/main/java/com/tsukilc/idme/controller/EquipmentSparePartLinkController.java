package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.EquipmentSparePartLinkCreateDTO;
import com.tsukilc.idme.entity.EquipmentSparePartLink;
import com.tsukilc.idme.service.EquipmentSparePartLinkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-spare-part-link")
public class EquipmentSparePartLinkController {
    private final EquipmentSparePartLinkService service;

    public EquipmentSparePartLinkController(EquipmentSparePartLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody EquipmentSparePartLinkCreateDTO dto) {
        return ApiResponse.success(service.create(dto));
    }

    @GetMapping("/by-equipment/{equipmentId}")
    public ApiResponse<List<EquipmentSparePartLink>> getByEquipment(@PathVariable String equipmentId) {
        return ApiResponse.success(service.getByEquipment(equipmentId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponse.success(null);
    }
}
