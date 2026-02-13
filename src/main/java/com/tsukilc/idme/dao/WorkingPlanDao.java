package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.WorkingPlan;
import org.springframework.stereotype.Repository;

/**
 * 工艺路线 DAO
 */
@Repository
public class WorkingPlanDao extends AbstractIdmeDao<WorkingPlan, String> {
    
    @Override
    protected String getEntityName() {
        return "WorkingPlan";
    }
    
    @Override
    protected Class<WorkingPlan> getEntityClass() {
        return WorkingPlan.class;
    }
}
