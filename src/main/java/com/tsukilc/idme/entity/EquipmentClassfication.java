package com.tsukilc.idme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备分类实体
 * SDK实体名：EquipmentClassfication（注意拼写）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentClassfication {
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
    private String equipmentClassName;      // 设备分类名称，必填
}
