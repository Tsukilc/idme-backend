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
    
    // === 业务字段 ===
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference parentPart;     // 父项物料（-> Part），必填
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference childPart;      // 子项物料（-> Part），必填
    
    private BigDecimal quantity;            // 用量，必填
    private String uom;                     // 单位
    private Integer findNumber;             // 项次（10/20/30）
    private LocalDate effectiveFrom;        // 生效时间
    private LocalDate effectiveTo;          // 失效时间
    private String remarks;                 // 备注
}
