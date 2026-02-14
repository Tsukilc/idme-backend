package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物料视图对象
 * 严格遵循 openapi.yaml 的 Part schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartVO {
    private String id;
    private String masterId;  // 主对象ID（用于版本管理）
    private String partNumber;  // 物料编号
    private String partName;  // 物料名称
    private String modelSpec;  // 规格型号
    private Integer stockQty;  // 库存数量
    private String unit;  // 计量单位ID
    private String supplierName;  // 供应商ID
    private String category;  // 物料分类ID
    private String businessVersion;  // 业务版本号
    private String versionNumber;  // 系统版本号（对应SDK的version字段，如"A.1"）
    private String description;  // 描述
    private String drawingUrl;  // 图纸链接
    private Object extra;  // 扩展信息
    private LocalDateTime createTime;
    private LocalDateTime lastModifiedTime;  // 对应SDK的lastUpdateTime
}
