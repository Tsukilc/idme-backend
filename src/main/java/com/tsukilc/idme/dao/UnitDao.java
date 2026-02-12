package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.Unit;
import org.springframework.stereotype.Repository;

/**
 * 计量单位 DAO
 */
@Repository
public class UnitDao extends AbstractIdmeDao<Unit, String> {
    
    @Override
    protected String getEntityName() {
        return "Unit";
    }
    
    @Override
    protected Class<Unit> getEntityClass() {
        return Unit.class;
    }
}
