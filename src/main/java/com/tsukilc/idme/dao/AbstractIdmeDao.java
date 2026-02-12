package com.tsukilc.idme.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tsukilc.idme.client.IdmeSdkClient;
import com.tsukilc.idme.client.dto.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * iDME DAO 抽象实现类
 * 实现通用 CRUD 逻辑，子类只需指定实体名称和类型
 * 
 * @param <T> 实体类型
 * @param <ID> 主键类型
 */
@Slf4j
public abstract class AbstractIdmeDao<T, ID> implements BaseIdmeDao<T, ID> {
    
    @Autowired
    protected IdmeSdkClient sdkClient;
    
    /**
     * 获取实体名称（如 "Employee"）
     * 子类必须实现
     */
    protected abstract String getEntityName();
    
    /**
     * 获取实体类型
     * 子类必须实现
     */
    protected abstract Class<T> getEntityClass();
    
    @Override
    public T create(T entity) {
        log.info("创建 {} 实体: {}", getEntityName(), entity);
        T result = sdkClient.create(getEntityName(), entity, getEntityClass());
        log.info("创建成功: {}", result);
        return result;
    }
    
    @Override
    public T update(T entity) {
        log.info("更新 {} 实体: {}", getEntityName(), entity);
        T result = sdkClient.update(getEntityName(), entity, getEntityClass());
        log.info("更新成功: {}", result);
        return result;
    }
    
    @Override
    public void delete(ID id) {
        log.info("删除 {} 实体，ID: {}", getEntityName(), id);
        sdkClient.delete(getEntityName(), id.toString());
        log.info("删除成功");
    }
    
    @Override
    public T findById(ID id) {
        log.info("查询 {} 实体，ID: {}", getEntityName(), id);
        T result = sdkClient.get(getEntityName(), id.toString(), getEntityClass());
        log.info("查询成功: {}", result);
        return result;
    }
    
    @Override
    public List<T> findAll(int pageNum, int pageSize) {
        log.info("分页查询 {} 列表，pageNum: {}, pageSize: {}", getEntityName(), pageNum, pageSize);
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setCondition(new HashMap<>());
        // 使用list接口，返回完整的基础属性（不需要手动指定字段）
        List<T> results = sdkClient.list(getEntityName(), queryRequest, pageNum, pageSize, getEntityClass());
        log.info("查询成功，共 {} 条", results != null ? results.size() : 0);
        return results;
    }
    
    @Override
    public List<T> findByCondition(Map<String, Object> condition, int pageNum, int pageSize) {
        log.info("条件查询 {}，条件: {}, pageNum: {}, pageSize: {}", 
            getEntityName(), condition, pageNum, pageSize);
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setCondition(condition);
        // 使用list接口，返回完整的基础属性
        List<T> results = sdkClient.list(getEntityName(), queryRequest, pageNum, pageSize, getEntityClass());
        log.info("查询成功，共 {} 条", results != null ? results.size() : 0);
        return results;
    }
}
