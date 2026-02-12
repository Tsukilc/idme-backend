package com.tsukilc.idme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 计量单位实体
 * 对应 sdk_entities.yaml 的 UnitViewDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
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
    private String unitName;           // 单位名称（如：件、米、千克等）
    private String unitDisplayName;    // 显示名称
    private String unitCategory;       // 单位分类
    private String unitFactor;         // 单位因子
    private String mesurementSystem;   // 测量系统
}
