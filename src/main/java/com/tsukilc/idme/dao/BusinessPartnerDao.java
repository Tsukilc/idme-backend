package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.BusinessPartner;
import org.springframework.stereotype.Repository;

/**
 * 往来单位 DAO
 */
@Repository
public class BusinessPartnerDao extends AbstractIdmeDao<BusinessPartner, String> {
    
    @Override
    protected String getEntityName() {
        return "BusinessPartner";
    }
    
    @Override
    protected Class<BusinessPartner> getEntityClass() {
        return BusinessPartner.class;
    }
}
