package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 设备 VO（用于前端展示）
 */
@Data
public class EquipmentVO {
    private String id;
    private String equipmentCode;           // 设备编码
    private String equipmentName;           // 设备名称
    
    private String manufacturerName;        // 生产厂家ID
    private String manufacturerDisplayName; // 生产厂家名称（展示用）
    
    private String brand;                   // 品牌
    private String modelSpec;               // 规格型号
    
    private String equipmentModelRef;       // 设备机型ID
    private String equipmentModelName;      // 设备机型名称（展示用）
    
    private String supplierName;            // 供应商ID
    private String supplierDisplayName;     // 供应商名称（展示用）
    
    private LocalDate productionDate;       // 生产日期
    private Integer serviceLifeYears;       // 使用年限
    private String depreciationMethod;      // 折旧方式
    private String locationText;            // 位置文本
    
    private String locationRef;             // 位置引用ID
    private String locationName;            // 位置名称（展示用）
    
    private String status;                  // 设备状态
    private String serialNumber;            // 序列号
    private String category;                // 设备分类
    private Map<String, Object> techParams; // 技术参数
    private String remarks;                 // 备注
    
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
