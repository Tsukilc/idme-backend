package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.ProcedureEquipmentLinkCreateDTO;
import com.tsukilc.idme.dto.ProcedureStatusUpdateDTO;
import com.tsukilc.idme.entity.ProcedureEquipmentLink;
import com.tsukilc.idme.service.ProcedureEquipmentLinkService;
import com.tsukilc.idme.vo.ProcedureEquipmentLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedure-equipment-link")
@Slf4j
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

    /**
     * 分页查询工序设备关联列表
     */
    @GetMapping
    public ApiResponse<PageResult<ProcedureEquipmentLinkVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<ProcedureEquipmentLinkVO> result = service.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 按 ID 查询工序设备关联
     */
    @GetMapping("/{id}")
    public ApiResponse<ProcedureEquipmentLinkVO> getById(@PathVariable String id) {
        log.info("查询工序设备关联，ID: {}", id);
        ProcedureEquipmentLinkVO link = service.getById(id);
        return ApiResponse.success(link);
    }

    /**
     * 更新工序设备关联
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable String id, @Validated @RequestBody ProcedureEquipmentLinkCreateDTO dto) {
        service.update(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 更新工序实际开始/结束时间
     * 对应：PATCH /api/procedure-equipment-link/{id}/actual-time
     */
    @PatchMapping("/{id}/actual-time")
    public ApiResponse<Void> updateActualTime(
            @PathVariable String id,
            @RequestBody ProcedureStatusUpdateDTO dto
    ) {
        log.info("更新工序实际时间，ID: {}", id);
        service.updateActualTime(id, dto);
        return ApiResponse.success(null);
    }
}
