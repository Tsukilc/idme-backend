package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "物料编号不能为空")
    private String partNumber;  // 物料编号

    @NotBlank(message = "物料名称不能为空")
    private String partName;  // 物料名称

    private String modelSpec;  // 规格型号

    private Integer stockQty;  // 库存数量

    @NotBlank(message = "计量单位不能为空")
    private String unit;  // 计量单位ID（SDK必填）

    @NotBlank(message = "供应商不能为空")
    private String supplierName;  // 供应商ID（SDK必填）

    @NotBlank(message = "物料分类不能为空")
    private String category;  // 物料分类ID（SDK必填）

    private String businessVersion;  // 业务版本号（展示用，如"1.0"）
    private String description;  // 描述
    private String drawingUrl;  // 图纸链接
    private Object extra;  // 扩展信息（JSON）
}
