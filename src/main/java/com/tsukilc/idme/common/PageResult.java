package com.tsukilc.idme.common;

import java.util.List;

/**
 * 分页结果包装类
 * 参照 docs/openapi.yaml 的 PageResult schema
 */
public class PageResult<T> {
    private Integer total;
    private Integer pageNum;
    private Integer pageSize;
    private List<T> records;
    
    public PageResult() {
    }
    
    public PageResult(Integer total, Integer pageNum, Integer pageSize, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.records = records;
    }
    
    // Getters and Setters
    public Integer getTotal() {
        return total;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public List<T> getRecords() {
        return records;
    }
    
    public void setRecords(List<T> records) {
        this.records = records;
    }
}
