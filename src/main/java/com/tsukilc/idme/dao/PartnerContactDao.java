package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.PartnerContact;
import org.springframework.stereotype.Repository;

/**
 * 联系人 DAO
 */
@Repository
public class PartnerContactDao extends AbstractIdmeDao<PartnerContact, String> {
    
    @Override
    protected String getEntityName() {
        return "PartnerContact";
    }
    
    @Override
    protected Class<PartnerContact> getEntityClass() {
        return PartnerContact.class;
    }
}
