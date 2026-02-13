package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 设备机型 VO（用于前端展示）
 */
@Data
public class EquipmentModelVO {
    private String id;
    private String modelCode;               // 机型编码
    private String modelName;               // 机型名称
    private String manufacturer;            // 默认厂家ID
    private String manufacturerName;        // 默认厂家名称（展示用）
    private String brand;                   // 默认品牌
    private String modelSpec;               // 默认规格型号
    private String category;                // 设备分类
    private Map<String, Object> defaultTechParams;  // 默认技术参数模板
    private String remarks;                 // 备注
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
