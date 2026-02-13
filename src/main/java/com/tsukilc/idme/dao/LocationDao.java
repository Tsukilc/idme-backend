package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.Location;
import org.springframework.stereotype.Repository;

/**
 * 位置 DAO
 * 支持树形结构查询
 */
@Repository
public class LocationDao extends AbstractIdmeDao<Location, String> {
    
    @Override
    protected String getEntityName() {
        return "Location";
    }
    
    @Override
    protected Class<Location> getEntityClass() {
        return Location.class;
    }
}
