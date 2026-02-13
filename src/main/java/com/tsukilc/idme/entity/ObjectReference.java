package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对象引用类
 * 对应 SDK 中的 ObjectReferenceParamDTO 和 ObjectReferenceViewDTO
 * 
 * SDK期待格式：{"id": "xxx"} 或 {"id": "xxx", "displayName": "xxx"}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // 忽略null字段
public class ObjectReference {
    private String id;
    private String displayName;  // 显示名称，从 ViewDTO 返回时包含，create时可为null
    private String name;  // 别名，某些SDK返回用name字段
    
    public ObjectReference(String id) {
        this.id = id;
        this.displayName = null;
        this.name = null;
    }
    
    public ObjectReference(String id, String className) {
        this.id = id;
        this.displayName = className;  // 用displayName存储className
        this.name = null;
    }
}
