package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工序-物料关联实体
 * 对应 schema.md 第8节
 * 支持双向查询和角色过滤
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedurePartLink {
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
    private ObjectReference procedure;      // 工序（-> WorkingProcedure），必填
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference part1;          // 物料（-> Part），必填
    
    private String role;                    // 投入/产出角色（Input/Output/辅料/工装夹具）
    private BigDecimal quantity;            // 数量
    private String uom;                     // 单位
    private Boolean isMandatory;            // 是否必需
    private String remarks;                 // 备注
}
