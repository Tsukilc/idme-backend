package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对象引用类
 * 对应 SDK 中的 ObjectReferenceParamDTO 和 ObjectReferenceViewDTO
 * 
 * 注意：序列化时只输出id（适配SDK的枚举类型字段），反序列化时从完整对象或字符串构造
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectReference {
    private String id;
    private String displayName;  // 显示名称，从 ViewDTO 返回时包含
    
    public ObjectReference(String id) {
        this.id = id;
    }
    
    /**
     * 序列化时只输出id字段（适配SDK的枚举类型参数）
     */
    @JsonValue
    public String toJsonValue() {
        return id;
    }
    
    /**
     * 反序列化时从字符串构造（当SDK返回的是简单字符串时）
     */
    @JsonCreator
    public static ObjectReference fromString(String value) {
        return new ObjectReference(value);
    }
}
