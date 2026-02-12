package com.tsukilc.idme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物料实体（版本对象）
 * 对应 SDK 的 PartViewDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Part {
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
    
    // === 版本对象字段 ===
    private ObjectReference master;  // 主对象引用
    private ObjectReference branch;  // 分支对象引用
    private Boolean latest;  // 是否最新有效版本
    private Boolean latestIteration;  // 是否最新迭代
    private Integer versionCode;  // 版本内码
    private Integer iteration;  // 迭代版本
    private String version;  // 版本号（系统生成，如"A.1"）
    private Boolean latestVersion;  // 是否最新版本
    private Boolean workingCopy;  // 是否工作副本（已检出）
    private String workingState;  // 工作状态：CHECKED_IN/CHECKED_OUT
    
    // === 业务字段 ===
    private String partNumber;  // 物料编号
    private String partName;  // 物料名称
    private String modelSpec;  // 规格型号
    
    // TODO: stockQty 根据SDK实际要求，可能需要调整为整数或特定精度
    private Integer stockQty;  // 库存数量（暂时用整数，避免浮点数精度问题）
    
    private ObjectReference unit;  // 计量单位（ObjectReference，关联Unit）
    
    private ObjectReference supplierName;  // 供应商（ObjectReference，关联BusinessPartner）
    private ObjectReference category;  // 物料分类（ObjectReference）
    
    private String businessVersion;  // 业务版本号（展示用，如"1.0"）
    private String description;  // 描述
    private String drawingUrl;  // 图纸链接
    private Object extra;  // 扩展信息（JSON）
}
