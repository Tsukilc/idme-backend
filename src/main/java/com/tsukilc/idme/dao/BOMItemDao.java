package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.BOMItem;
import org.springframework.stereotype.Repository;

/**
 * BOM项 DAO
 */
@Repository
public class BOMItemDao extends AbstractIdmeDao<BOMItem, String> {
    
    @Override
    protected String getEntityName() {
        return "BOMItem";
    }
    
    @Override
    protected Class<BOMItem> getEntityClass() {
        return BOMItem.class;
    }
}
