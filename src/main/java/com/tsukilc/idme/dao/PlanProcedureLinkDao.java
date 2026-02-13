package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.PlanProcedureLink;
import org.springframework.stereotype.Repository;

@Repository
public class PlanProcedureLinkDao extends AbstractIdmeDao<PlanProcedureLink, String> {
    @Override
    protected String getEntityName() {
        return "PlanProcedureLink";
    }
    
    @Override
    protected Class<PlanProcedureLink> getEntityClass() {
        return PlanProcedureLink.class;
    }
}
