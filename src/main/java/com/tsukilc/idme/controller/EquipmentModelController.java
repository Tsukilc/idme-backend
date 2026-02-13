package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.EquipmentModelCreateDTO;
import com.tsukilc.idme.service.EquipmentModelService;
import com.tsukilc.idme.vo.EquipmentModelVO;
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
 * 设备机型 Controller
 */
@RestController
@RequestMapping("/api/equipment-model")
public class EquipmentModelController {

    private final EquipmentModelService equipmentModelService;

    public EquipmentModelController(EquipmentModelService equipmentModelService) {
        this.equipmentModelService = equipmentModelService;
    }

    /**
     * 分页查询设备机型列表
     */
    @GetMapping
    public ApiResponse<PageResult<EquipmentModelVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<EquipmentModelVO> result = equipmentModelService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建设备机型
     */
    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody EquipmentModelCreateDTO dto) {
        String id = equipmentModelService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 查询设备机型详情
     */
    @GetMapping("/{id}")
    public ApiResponse<EquipmentModelVO> getById(@PathVariable String id) {
        EquipmentModelVO vo = equipmentModelService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 更新设备机型
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable String id, @Validated @RequestBody EquipmentModelCreateDTO dto) {
        equipmentModelService.update(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 删除设备机型
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        equipmentModelService.delete(id);
        return ApiResponse.success(null);
    }
}
