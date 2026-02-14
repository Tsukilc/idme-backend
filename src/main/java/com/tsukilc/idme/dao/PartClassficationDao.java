package com.tsukilc.idme.dao;

import com.tsukilc.idme.entity.PartClassfication;
import org.springframework.stereotype.Repository;

/**
 * 物料分类 DAO
 */
@Repository
public class PartClassficationDao extends AbstractIdmeDao<PartClassfication, String> {

    @Override
    protected String getEntityName() {
        return "PartClassfication";
    }

    @Override
    protected Class<PartClassfication> getEntityClass() {
        return PartClassfication.class;
    }
}
