package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.DepartmentCreateDTO;
import com.tsukilc.idme.service.DepartmentService;
import com.tsukilc.idme.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部门管理 Controller
 * 严格参照 docs/openapi.yaml 定义接口路径和参数
 */
@RestController
@RequestMapping("/api/department")
@Slf4j
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 创建部门
     * 对应：POST /api/department
     */
    @PostMapping
    public ApiResponse<DepartmentVO> create(@RequestBody DepartmentCreateDTO dto) {
        log.info("创建部门: {}", dto);
        DepartmentVO vo = departmentService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询部门详情
     * 对应：GET /api/department/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<DepartmentVO> getById(@PathVariable String id) {
        log.info("查询部门详情，ID: {}", id);
        DepartmentVO vo = departmentService.getById(id);
        return ApiResponse.success(vo);
    }
    
    /**
     * 更新部门
     * 对应：PUT /api/department/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<DepartmentVO> update(
            @PathVariable String id,
            @RequestBody DepartmentCreateDTO dto
    ) {
        log.info("更新部门，ID: {}, 数据: {}", id, dto);
        DepartmentVO vo = departmentService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除部门
     * 对应：DELETE /api/department/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除部门，ID: {}", id);
        departmentService.delete(id);
        return ApiResponse.success(null);
    }
}
