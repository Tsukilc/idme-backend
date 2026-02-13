package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 设备实体
 * 对应 schema.md 第1节
 * 竞赛核心模块（10分）- 支持技术参数和备品备件扩展
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
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
    private String equipmentCode;           // 设备编码，必填，唯一
    private String equipmentName;           // 设备名称，必填
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference manufacturerName;   // 生产厂家（-> BusinessPartner）
    
    private String brand;                   // 品牌
    private String modelSpec;               // 规格型号
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference equipmentModelRef;  // 设备机型（-> EquipmentModel）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference supplierName;   // 供应商（-> BusinessPartner）
    
    private LocalDate productionDate;       // 生产日期
    private Integer serviceLifeYears;       // 使用年限
    private String depreciationMethod;      // 折旧方式（枚举：直线法/双倍余额递减/年数总和/不折旧）
    private String locationText;            // 位置文本（评分字段）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference locationRef;    // 位置引用（-> Location）
    
    private String status;                  // 设备状态（枚举：运行/待机/维修/停机/报废）
    private String serialNumber;            // 序列号
    private String category;                // 设备分类
    private Map<String, Object> techParams; // 技术参数（JSON，快捷扩展）
    private String remarks;                 // 备注
}
