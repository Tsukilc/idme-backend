package com.tsukilc.idme.client.dto;

import java.util.List;

/**
 * iDME SDK 响应包装类
 */
public class RdmResponse<T> {
    private String result;  // SUCCESS/FAIL
    private T data;
    private List<String> errors;
    
    public RdmResponse() {
    }
    
    public RdmResponse(String result, T data, List<String> errors) {
        this.result = result;
        this.data = data;
        this.errors = errors;
    }
    
    // Getters and Setters
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
