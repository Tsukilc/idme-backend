package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Employee DAO 实现
 */
@Repository
@Slf4j
public class EmployeeDao extends AbstractIdmeDao<Employee, String> {
    
    @Override
    protected String getEntityName() {
        return "Employee";
    }
    
    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }
    
    /**
     * 按部门查询员工（使用旧的findByCondition接口）
     * 对应 API: GET /api/employee/by-dept/{deptId}
     * 注意：SDK条件查询可能不支持复杂字段过滤
     */
    public List<Employee> findByDept(String deptId, int pageNum, int pageSize) {
        log.info("按部门查询员工（findByCondition），deptId: {}", deptId);
        Map<String, Object> condition = new HashMap<>();
        condition.put("dept.id", deptId);
        return findByCondition(condition, pageNum, pageSize);
    }

    /**
     * 按部门查询员工（使用新的find接口）
     * 测试find接口的filter字段用法
     */
    public List<Employee> findByDeptUsingFind(String deptId, int pageNum, int pageSize) {
        log.info("按部门查询员工（find接口），deptId: {}", deptId);

        // 构建filter条件
        Map<String, Object> filter = new HashMap<>();
        filter.put("joiner", "and");

        // 尝试方式1：使用"dept.id"作为conditionName（嵌套字段）
        List<Map<String, Object>> conditions = new java.util.ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("conditionName", "dept.id");  // 测试：是否支持嵌套字段
        condition.put("operator", "=");
        condition.put("conditionValues", java.util.Arrays.asList(deptId));
        conditions.add(condition);

        filter.put("conditions", conditions);

        // 调用SDK的find接口
        try {
            return sdkClient.find("Employee", filter, null, pageNum, pageSize, Employee.class);
        } catch (Exception e) {
            log.warn("使用find接口查询失败（dept.id），尝试使用dept: {}", e.getMessage());

            // 如果失败，尝试方式2：使用"dept"作为conditionName
            condition.put("conditionName", "dept");
            conditions.clear();
            conditions.add(condition);

            return sdkClient.find("Employee", filter, null, pageNum, pageSize, Employee.class);
        }
    }
    
    /**
     * 按员工编号查询
     */
    public Employee findByEmployeeNo(String employeeNo) {
        log.info("按员工编号查询，employeeNo: {}", employeeNo);
        Map<String, Object> condition = new HashMap<>();
        condition.put("employeeNo", employeeNo);
        List<Employee> results = findByCondition(condition, 1, 1);
        return results != null && !results.isEmpty() ? results.get(0) : null;
    }
    
    /**
     * 按状态查询员工
     */
    public List<Employee> findByStatus(String status, int pageNum, int pageSize) {
        log.info("按状态查询员工，status: {}", status);
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", status);
        return findByCondition(condition, pageNum, pageSize);
    }
}
