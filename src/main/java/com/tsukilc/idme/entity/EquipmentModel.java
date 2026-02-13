package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 设备机型实体
 * 对应 schema.md 第14节
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentModel {
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
    private String modelCode;               // 机型编码，必填，唯一
    private String modelName;               // 机型名称，必填
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference manufacturer;   // 默认厂家（-> BusinessPartner）
    
    private String brand;                   // 默认品牌
    private String modelSpec;               // 默认规格型号
    private String category;                // 设备分类
    private Map<String, Object> defaultTechParams;  // 默认技术参数模板（JSON）
    private String remarks;                 // 备注
}
