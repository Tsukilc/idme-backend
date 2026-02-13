package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 位置创建/更新 DTO
 */
@Data
public class LocationCreateDTO {
    @NotBlank(message = "位置编码不能为空")
    private String locationCode;
    
    @NotBlank(message = "位置名称不能为空")
    private String locationName;
    
    @NotBlank(message = "位置类型不能为空")
    private String locationType;  // Plant/Workshop/Line/Station/Warehouse
    
    private String parentLocation;  // 上级位置ID（可选，用于支持树形结构）
    
    private String addressText;     // 地址描述
    
    private String manager;         // 负责人ID（-> Employee，可选）
    
    private String remarks;         // 备注
}
