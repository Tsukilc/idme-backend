package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * BOM 项实体（物料组成关系）
 * 对应 schema.md 第5节
 * 竞赛核心模块（5分）- 支持树形查询和批量创建
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOMItem {
    // === SDK 系统字段 ===
    private String id;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
    private Integer rdmVersion;
    private Integer rdmDeleteFlag;
    private String rdmExtensionType;
    private String className;
    
    // === Link关系字段（双重命名）===
    // SDK标准字段：source/target (必填)
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference source;         // 源对象（-> Part子件）

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference target;         // 目标对象（-> Part父件）

    // 业务别名字段（需要同时设置）
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference parentPart;     // 父项物料（与target同值）

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference childPart;      // 子项物料（与source同值）

    // === 业务字段 ===
    private Object quantity;                // 用量（SDK格式：{value: "1"}）

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference uom;            // 计量单位（-> Unit）
    private Integer findNumber;             // 项次（10/20/30）
    private Object effectiveFrom;           // 生效时间（SDK可能返回Date或DateTime格式）
    private Object effectiveTo;             // 失效时间（SDK可能返回Date或DateTime格式）
    private String remarks;                 // 备注
}
