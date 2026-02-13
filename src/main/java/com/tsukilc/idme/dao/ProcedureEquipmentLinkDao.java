package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.ProcedureEquipmentLink;
import org.springframework.stereotype.Repository;

@Repository
public class ProcedureEquipmentLinkDao extends AbstractIdmeDao<ProcedureEquipmentLink, String> {
    @Override
    protected String getEntityName() {
        return "ProcedureEquipmentLink";
    }
    
    @Override
    protected Class<ProcedureEquipmentLink> getEntityClass() {
        return ProcedureEquipmentLink.class;
    }
}
