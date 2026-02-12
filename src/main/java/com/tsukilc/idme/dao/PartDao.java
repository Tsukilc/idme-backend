package com.tsukilc.idme.dao;

import com.tsukilc.idme.client.IdmeSdkClient;
import com.tsukilc.idme.entity.Part;
import org.springframework.stereotype.Repository;

/**
 * 物料 DAO
 * Part是版本对象(VersionObject)，需要特殊处理
 */
@Repository
public class PartDao extends AbstractIdmeDao<Part, String> {
    
    @Override
    protected String getEntityName() {
        return "Part";
    }
    
    @Override
    protected Class<Part> getEntityClass() {
        return Part.class;
    }
    
    /**
     * 按物料编号查询（查询主对象下的所有版本）
     */
    public java.util.List<Part> findByPartNumber(String partNumber, int pageNum, int pageSize) {
        java.util.Map<String, Object> condition = new java.util.HashMap<>();
        condition.put("partNumber", partNumber);
        return findByCondition(condition, pageNum, pageSize);
    }
    
    /**
     * 查询最新版本
     */
    public java.util.List<Part> findLatest(int pageNum, int pageSize) {
        java.util.Map<String, Object> condition = new java.util.HashMap<>();
        condition.put("latest", true);
        return findByCondition(condition, pageNum, pageSize);
    }
}
