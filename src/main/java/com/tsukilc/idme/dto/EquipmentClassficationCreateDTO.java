package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 设备分类创建/更新 DTO
 */
@Data
public class EquipmentClassficationCreateDTO {
    @NotBlank(message = "设备分类名称不能为空")
    private String equipmentClassName;      // 设备分类名称，必填
}
