package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.Equipment;
import org.springframework.stereotype.Repository;

/**
 * 设备 DAO
 */
@Repository
public class EquipmentDao extends AbstractIdmeDao<Equipment, String> {
    
    @Override
    protected String getEntityName() {
        return "Equipment";
    }
    
    @Override
    protected Class<Equipment> getEntityClass() {
        return Equipment.class;
    }
}
