package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 物料分类创建/更新 DTO
 */
@Data
public class PartClassficationCreateDTO {
    @NotBlank(message = "物料分类名称不能为空")
    private String partClassName;  // 物料分类名称
}
