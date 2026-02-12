package com.tsukilc.idme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物料创建/更新 DTO
 * 严格参照 openapi.yaml 的 PartCreate schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartCreateDTO {
    private String partNumber;  // 物料编号
    private String partName;  // 物料名称
    private String modelSpec;  // 规格型号
    
    // TODO: 根据SDK实际要求调整数据类型
    private Integer stockQty;  // 库存数量（暂时用整数）
    
    private String unit;  // 计量单位
    
    private String supplierName;  // 供应商ID（字符串，前端传入BusinessPartner的ID）
    private String category;  // 物料分类ID（字符串，前端传入分类ID）
    
    private String businessVersion;  // 业务版本号（展示用，如"1.0"）
    private String description;  // 描述
    private String drawingUrl;  // 图纸链接
    private Object extra;  // 扩展信息（JSON）
}
