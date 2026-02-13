package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.WorkingProcedure;
import org.springframework.stereotype.Repository;

/**
 * 工序 DAO
 */
@Repository
public class WorkingProcedureDao extends AbstractIdmeDao<WorkingProcedure, String> {
    
    @Override
    protected String getEntityName() {
        return "WorkingProcedure";
    }
    
    @Override
    protected Class<WorkingProcedure> getEntityClass() {
        return WorkingProcedure.class;
    }
}
