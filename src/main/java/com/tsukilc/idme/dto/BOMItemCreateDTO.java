package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * BOM项创建/更新 DTO
 */
@Data
public class BOMItemCreateDTO {
    @NotBlank(message = "父项物料不能为空")
    private String parentPart;              // 父项物料ID（-> Part），必填
    
    @NotBlank(message = "子项物料不能为空")
    private String childPart;               // 子项物料ID（-> Part），必填
    
    @NotNull(message = "用量不能为空")
    private BigDecimal quantity;            // 用量，必填
    
    private String uom;                     // 单位
    private Integer findNumber;             // 项次
    private LocalDate effectiveFrom;        // 生效时间
    private LocalDate effectiveTo;          // 失效时间
    private String remarks;                 // 备注
}
