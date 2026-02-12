package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.Department;
import org.springframework.stereotype.Repository;

/**
 * 部门 DAO
 */
@Repository
public class DepartmentDao extends AbstractIdmeDao<Department, String> {
    
    @Override
    protected String getEntityName() {
        return "Department";
    }
    
    @Override
    protected Class<Department> getEntityClass() {
        return Department.class;
    }
}
