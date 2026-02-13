package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备-备品备件关联实体
 * 对应 schema.md 第15节
 * 关联Equipment评分点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentSparePartLink {
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
    private ObjectReference equipment;      // 设备（-> Equipment），必填
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference sparePart;      // 备件物料（-> Part），必填
    
    private Integer quantity;               // 配置数量
    private String unit;                    // 单位
    private Boolean isCritical;             // 关键备件
    private Integer replacementCycleDays;   // 更换周期(天)
    private String remarks;                 // 备注
}
