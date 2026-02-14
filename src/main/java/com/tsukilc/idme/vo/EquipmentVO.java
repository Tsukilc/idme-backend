package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备 VO（严格遵循openapi.yaml的Equipment schema）
 */
@Data
public class EquipmentVO {
    private String id;
    private String equipmentCode;        // 设备编码
    private String equipmentName;        // 设备名称
    private String manufacturerName;     // 生产厂家ID
    private String brand;                // 品牌
    private String modelSpec;            // 规格型号
    private String supplierName;         // 供应商ID
    private LocalDateTime productionDate;// 生产日期
    private Integer serviceLifeYears;    // 使用年限
    private String depreciationMethod;   // 折旧方式
    private String locationText;         // 位置文本
    private String locationRef;          // 位置引用ID
    private String status;               // 设备状态
    private String serialNumber;         // 序列号
    private String category;             // 设备分类ID
    private Object techParams;           // 技术参数JSON
    private String remarks;              // 备注
    private LocalDateTime createTime;
    private LocalDateTime lastModifiedTime;
}
