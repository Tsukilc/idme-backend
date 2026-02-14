package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 对象引用类
 * 对应 SDK 中的 ObjectReferenceParamDTO 和 ObjectReferenceViewDTO
 *
 * SDK期待格式：{"id": "xxx"} 或 {"id": "xxx", "displayName": "xxx"}
 * 版本对象需要支持：tenant、needSetNullAttrs字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // 忽略null字段
public class ObjectReference {
    private String id;
    private String displayName;  // 显示名称，从 ViewDTO 返回时包含，create时可为null
    private String name;  // 别名，某些SDK返回用name字段

    // 版本对象专用字段
    private Map<String, Object> tenant;  // 租户信息
    private List<String> needSetNullAttrs;  // 需要设置为null的属性列表

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
