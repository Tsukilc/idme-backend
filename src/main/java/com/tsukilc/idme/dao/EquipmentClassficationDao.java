package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.EquipmentClassfication;
import org.springframework.stereotype.Repository;

/**
 * 设备分类 DAO
 */
@Repository
public class EquipmentClassficationDao extends AbstractIdmeDao<EquipmentClassfication, String> {

    @Override
    protected String getEntityName() {
        return "EquipmentClassfication";
    }

    @Override
    protected Class<EquipmentClassfication> getEntityClass() {
        return EquipmentClassfication.class;
    }
}
