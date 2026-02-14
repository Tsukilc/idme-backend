package com.tsukilc.idme.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量添加工序到工艺路线 DTO
 */
@Data
public class PlanProcedureLinkBatchDTO {
    @NotBlank(message = "工艺路线 ID 不能为空")
    private String planId;

    @NotEmpty(message = "工序列表不能为空")
    @Valid
    private List<ProcedureItem> procedures;

    @Data
    public static class ProcedureItem {
        @NotBlank(message = "工序 ID 不能为空")
        private String procedureId;

        private Integer sequenceNo;            // 顺序号
        private Integer standardDurationMin;   // 标准工时（分钟）
        private String requirement;            // 工艺要求
    }
}
