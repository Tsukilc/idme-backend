package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.EquipmentModel;
import org.springframework.stereotype.Repository;

/**
 * 设备机型 DAO
 */
@Repository
public class EquipmentModelDao extends AbstractIdmeDao<EquipmentModel, String> {
    
    @Override
    protected String getEntityName() {
        return "EquipmentModel";
    }
    
    @Override
    protected Class<EquipmentModel> getEntityClass() {
        return EquipmentModel.class;
    }
}
