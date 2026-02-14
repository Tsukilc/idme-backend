package com.tsukilc.idme.vo;

import lombok.Data;

import java.util.Map;

/**
 * 设备机型 VO（严格遵循openapi.yaml的EquipmentModel schema）
 */
@Data
public class EquipmentModelVO {
    private String id;
    private String modelCode;               // 机型编码
    private String modelName;               // 机型名称
    private String manufacturer;            // 默认厂家ID
    private String brand;                   // 默认品牌
    private String modelSpec;               // 默认规格型号
    private String category;                // 设备分类ID
    private Map<String, Object> defaultTechParams;  // 默认技术参数模板
    private String remarks;                 // 备注
}
