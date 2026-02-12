package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.EmployeeCreateDTO;
import com.tsukilc.idme.service.EmployeeService;
import com.tsukilc.idme.vo.EmployeeVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理 Controller
 * 严格参照 docs/openapi.yaml 定义接口路径和参数
 */
@RestController
@RequestMapping("/api/employee")  // 注意：单数形式
@Validated
@Slf4j
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * 分页查询员工列表
     * 对应：GET /api/employee
     */
    @GetMapping
    public ApiResponse<PageResult<EmployeeVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String employeeNo,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String status
    ) {
        log.info("查询员工列表，pageNum: {}, pageSize: {}, employeeName: {}, employeeNo: {}, dept: {}, status: {}",
            pageNum, pageSize, employeeName, employeeNo, dept, status);
        
        // TODO: 处理查询条件
        List<EmployeeVO> list = employeeService.list(pageNum, pageSize);
        
        // 构建分页结果
        PageResult<EmployeeVO> pageResult = new PageResult<>(
            list.size(), pageNum, pageSize, list
        );
        
        return ApiResponse.success(pageResult);
    }
    
    /**
     * 创建员工
     * 对应：POST /api/employee
     */
    @PostMapping
    public ApiResponse<EmployeeVO> create(@RequestBody @Valid EmployeeCreateDTO dto) {
        log.info("创建员工: {}", dto);
        EmployeeVO vo = employeeService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询员工详情
     * 对应：GET /api/employee/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<EmployeeVO> getById(@PathVariable String id) {
        log.info("查询员工详情，ID: {}", id);
        EmployeeVO vo = employeeService.getById(id);
        return ApiResponse.success(vo);
    }
    
    /**
     * 更新员工
     * 对应：PUT /api/employee/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<EmployeeVO> update(
            @PathVariable String id,
            @RequestBody @Valid EmployeeCreateDTO dto
    ) {
        log.info("更新员工，ID: {}, 数据: {}", id, dto);
        EmployeeVO vo = employeeService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除员工
     * 对应：DELETE /api/employee/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除员工，ID: {}", id);
        employeeService.delete(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 按部门查询员工
     * 对应：GET /api/employee/by-dept/{deptId}
     */
    @GetMapping("/by-dept/{deptId}")
    public ApiResponse<List<EmployeeVO>> listByDept(@PathVariable String deptId) {
        log.info("按部门查询员工，deptId: {}", deptId);
        List<EmployeeVO> list = employeeService.findByDept(deptId);
        return ApiResponse.success(list);
    }
}
