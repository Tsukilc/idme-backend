package com.tsukilc.idme.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class ProcedurePartLinkCreateDTO {
    @NotBlank(message = "工序不能为空")
    private String procedure;               // 工序ID
    
    @NotBlank(message = "物料不能为空")
    private String part1;                   // 物料ID
    
    private String role;                    // 角色
    private BigDecimal quantity;            // 数量
    private String uom;                     // 单位
    private Boolean isMandatory;            // 是否必需
    private String remarks;
}
