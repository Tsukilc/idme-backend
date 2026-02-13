package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.ProcedurePartLink;
import org.springframework.stereotype.Repository;

@Repository
public class ProcedurePartLinkDao extends AbstractIdmeDao<ProcedurePartLink, String> {
    @Override
    protected String getEntityName() {
        return "ProcedurePartLink";
    }
    
    @Override
    protected Class<ProcedurePartLink> getEntityClass() {
        return ProcedurePartLink.class;
    }
}
