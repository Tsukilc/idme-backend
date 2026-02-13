package com.tsukilc.idme.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * workingState字段引用类型
 * SDK行为：
 * - 创建/更新时：接受String枚举值（CHECKED_IN/CHECKED_OUT）
 * - 查询时：返回Object {code, cnName, enName, alias}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = WorkingStateRefSerializer.class)
@JsonDeserialize(using = WorkingStateRefDeserializer.class)
public class WorkingStateRef {
    private String code;        // CHECKED_IN / CHECKED_OUT
    private String cnName;      // 已检入 / 已检出
    private String enName;      // checked in / checked out
    private String alias;       // CHECKED_IN / CHECKED_OUT

    /**
     * 从枚举字符串创建（用于DTO转Entity）
     */
    public WorkingStateRef(String code) {
        this.code = code;
        this.enName = code;
    }
    
    /**
     * 获取枚举值（用于序列化到SDK）
     */
    public String getCode() {
        return code;
    }
}
