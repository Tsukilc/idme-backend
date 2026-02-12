package com.tsukilc.idme.dao;

import java.util.List;
import java.util.Map;

/**
 * iDME DAO 基础接口
 * 定义通用 CRUD 操作
 * 
 * @param <T> 实体类型
 * @param <ID> 主键类型
 */
public interface BaseIdmeDao<T, ID> {
    
    /**
     * 创建实体
     */
    T create(T entity);
    
    /**
     * 更新实体
     */
    T update(T entity);
    
    /**
     * 删除实体
     */
    void delete(ID id);
    
    /**
     * 根据ID查询
     */
    T findById(ID id);
    
    /**
     * 分页查询所有
     */
    List<T> findAll(int pageNum, int pageSize);
    
    /**
     * 根据条件查询
     */
    List<T> findByCondition(Map<String, Object> condition, int pageNum, int pageSize);
}
