package com.tsukilc.idme.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class EquipmentSparePartLinkCreateDTO {
    @NotBlank(message = "设备不能为空")
    private String equipment;               // 设备ID
    
    @NotBlank(message = "备件物料不能为空")
    private String sparePart;               // 备件物料ID
    
    private Integer quantity;               // 配置数量
    private String unit;                    // 单位
    private Boolean isCritical;             // 关键备件
    private Integer replacementCycleDays;   // 更换周期
    private String remarks;
}
