package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 设备机型创建/更新 DTO
 */
@Data
public class EquipmentModelCreateDTO {
    @NotBlank(message = "机型编码不能为空")
    private String modelCode;               // 机型编码，必填，唯一
    
    @NotBlank(message = "机型名称不能为空")
    private String modelName;               // 机型名称，必填
    
    @NotBlank(message = "默认厂家不能为空")
    private String manufacturer;            // 默认厂家ID（-> BusinessPartner），SDK必填

    private String brand;                   // 默认品牌
    private String modelSpec;               // 默认规格型号
    private String category;                // 设备分类ID（-> EquipmentClassfication，可选）
    private Map<String, Object> defaultTechParams;  // 默认技术参数模板（JSON）
    private String remarks;                 // 备注
}
