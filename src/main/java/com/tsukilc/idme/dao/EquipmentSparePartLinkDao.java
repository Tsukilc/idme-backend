package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.EquipmentSparePartLink;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentSparePartLinkDao extends AbstractIdmeDao<EquipmentSparePartLink, String> {
    @Override
    protected String getEntityName() {
        return "EquipmentSparePartLink";
    }
    
    @Override
    protected Class<EquipmentSparePartLink> getEntityClass() {
        return EquipmentSparePartLink.class;
    }
}
