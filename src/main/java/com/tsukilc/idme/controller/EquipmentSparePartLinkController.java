package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.EquipmentSparePartLinkCreateDTO;
import com.tsukilc.idme.service.EquipmentSparePartLinkService;
import com.tsukilc.idme.vo.EquipmentSparePartLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/equipment-spare-part-link")
@Slf4j
public class EquipmentSparePartLinkController {
    private final EquipmentSparePartLinkService service;

    public EquipmentSparePartLinkController(EquipmentSparePartLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<EquipmentSparePartLinkVO> create(@Valid @RequestBody EquipmentSparePartLinkCreateDTO dto) {
        log.info("创建设备-备件关联请求: {}", dto);
        return ApiResponse.success(service.create(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<EquipmentSparePartLinkVO> update(@PathVariable String id,
                                                         @Valid @RequestBody EquipmentSparePartLinkCreateDTO dto) {
        log.info("更新设备-备件关联请求，ID: {}, DTO: {}", id, dto);
        return ApiResponse.success(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<EquipmentSparePartLinkVO> getById(@PathVariable String id) {
        log.info("查询设备备件关联，ID: {}", id);
        return ApiResponse.success(service.getById(id));
    }

    @GetMapping
    public ApiResponse<List<EquipmentSparePartLinkVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("分页查询设备-备件关联列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        return ApiResponse.success(service.list(pageNum, pageSize));
    }

    @GetMapping("/by-equipment/{equipmentId}")
    public ApiResponse<List<EquipmentSparePartLinkVO>> getByEquipment(@PathVariable String equipmentId) {
        log.info("按设备查询关联，equipmentId: {}", equipmentId);
        return ApiResponse.success(service.getByEquipment(equipmentId));
    }

    @GetMapping("/by-part/{partId}")
    public ApiResponse<List<EquipmentSparePartLinkVO>> getByPart(@PathVariable String partId) {
        log.info("按物料查询关联，partId: {}", partId);
        return ApiResponse.success(service.getByPart(partId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除设备-备件关联，ID: {}", id);
        service.delete(id);
        return ApiResponse.success(null);
    }
}
