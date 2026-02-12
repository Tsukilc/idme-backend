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
     * 按部门查询员工
     * 对应 API: GET /api/employee/by-dept/{deptId}
     */
    public List<Employee> findByDept(String deptId, int pageNum, int pageSize) {
        log.info("按部门查询员工，deptId: {}", deptId);
        Map<String, Object> condition = new HashMap<>();
        condition.put("dept.id", deptId);
        return findByCondition(condition, pageNum, pageSize);
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
