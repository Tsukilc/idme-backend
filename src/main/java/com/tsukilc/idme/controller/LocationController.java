package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.LocationCreateDTO;
import com.tsukilc.idme.service.LocationService;
import com.tsukilc.idme.vo.LocationVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 位置管理 Controller
 * 严格参照 docs/openapi.yaml 定义接口路径和参数
 * 支持树形结构查询
 */
@RestController
@RequestMapping("/api/location")
@Slf4j
public class LocationController {
    
    @Autowired
    private LocationService locationService;

    /**
     * 分页查询位置列表
     * 对应：GET /api/location?pageNum=1&pageSize=20
     */
    @GetMapping
    public ApiResponse<PageResult<LocationVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("查询位置列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        PageResult<LocationVO> result = locationService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建位置
     * 对应：POST /api/location
     */
    @PostMapping
    public ApiResponse<LocationVO> create(@RequestBody @Valid LocationCreateDTO dto) {
        log.info("创建位置: {}", dto);
        LocationVO vo = locationService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询位置详情
     * 对应：GET /api/location/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<LocationVO> getById(@PathVariable String id) {
        log.info("查询位置详情，ID: {}", id);
        LocationVO vo = locationService.getById(id);
        return ApiResponse.success(vo);
    }
    
    /**
     * 更新位置
     * 对应：PUT /api/location/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<LocationVO> update(
            @PathVariable String id,
            @RequestBody @Valid LocationCreateDTO dto
    ) {
        log.info("更新位置，ID: {}, 数据: {}", id, dto);
        LocationVO vo = locationService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除位置
     * 对应：DELETE /api/location/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除位置，ID: {}", id);
        locationService.delete(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 查询位置树
     * 对应：GET /api/location/tree
     */
    @GetMapping("/tree")
    public ApiResponse<List<LocationService.LocationTreeVO>> getTree() {
        log.info("查询位置树");
        List<LocationService.LocationTreeVO> tree = locationService.getTree();
        return ApiResponse.success(tree);
    }
    
    /**
     * 查询某位置下的所有设备
     * 对应：GET /api/location/{id}/equipments
     * TODO: 等待 Equipment 模块实现后再完善
     */
    @GetMapping("/{id}/equipments")
    public ApiResponse<List<Object>> getLocationEquipments(@PathVariable String id) {
        log.info("查询位置下的设备，位置ID: {}", id);
        // TODO: 调用 EquipmentService 查询该位置下的所有设备
        return ApiResponse.success(List.of());
    }
}
