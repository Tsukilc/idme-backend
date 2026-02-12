package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量单位创建/更新 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitCreateDTO {
    @NotBlank(message = "单位名称不能为空")
    private String unitName;           // 单位名称
    
    private String unitDisplayName;    // 显示名称
    private String unitCategory;       // 单位分类
    private String unitFactor;         // 单位因子
    private String mesurementSystem;   // 测量系统
}
