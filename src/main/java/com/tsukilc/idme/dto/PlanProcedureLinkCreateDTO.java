package com.tsukilc.idme.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PlanProcedureLinkCreateDTO {
    @NotBlank(message = "工艺路线不能为空")
    private String plan;                    // 工艺路线ID
    
    @NotBlank(message = "工序不能为空")
    private String procedure;               // 工序ID
    
    @NotNull(message = "顺序号不能为空")
    private Integer sequenceNo;             // 顺序号
    
    private BigDecimal standardDurationMin; // 标准工时
    private String requirement;             // 工艺要求
}
