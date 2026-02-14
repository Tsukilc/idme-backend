package com.tsukilc.idme.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ProcedurePartLinkCreateDTO {
    @NotBlank(message = "工序不能为空")
    private String procedure;               // 工序ID

    @NotBlank(message = "物料不能为空")
    private String part1;                   // 物料ID

    private String role;                    // 角色
    private Integer quantity;               // 数量（Integer类型，与EquipmentSparePartLink一致）
    private String uom;                     // 单位
    private Boolean isMandatory;            // 是否必需
    private String remarks;
}
