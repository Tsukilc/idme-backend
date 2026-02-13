package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.EquipmentCreateDTO;
import com.tsukilc.idme.service.EquipmentService;
import com.tsukilc.idme.vo.EquipmentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备 Controller
 * 竞赛核心模块（10分）
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * 分页查询设备列表
     */
    @GetMapping
    public ApiResponse<PageResult<EquipmentVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<EquipmentVO> result = equipmentService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建设备
     */
    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody EquipmentCreateDTO dto) {
        String id = equipmentService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 查询设备详情
     */
    @GetMapping("/{id}")
    public ApiResponse<EquipmentVO> getById(@PathVariable String id) {
        EquipmentVO vo = equipmentService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 更新设备
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable String id, @Validated @RequestBody EquipmentCreateDTO dto) {
        equipmentService.update(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        equipmentService.delete(id);
        return ApiResponse.success(null);
    }
}
