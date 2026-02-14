package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    
    private LocalDateTime productionDate;   // 生产日期（SDK使用时间戳）
    private Integer serviceLifeYears;       // 使用年限
    private Object depreciationMethod;      // 折旧方式（SDK返回Map结构 {code, cnName, enName, alias}）
    private String locationText;            // 位置文本（评分字段）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference locationRef;    // 位置引用（-> Location）
    
    private Object status;                  // 设备状态（SDK返回Map结构 {code, cnName, enName, alias}）
    private String serialNumber;            // 序列号

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference category;       // 设备分类（-> EquipmentClassfication，可选）
    private Object techParams;              // 技术参数（SDK返回数组格式）
    private String remarks;                 // 备注
}
