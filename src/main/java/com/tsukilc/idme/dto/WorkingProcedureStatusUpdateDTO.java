package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 工序状态更新 DTO
 * 支持竞赛要求的状态更新功能
 */
@Data
public class WorkingProcedureStatusUpdateDTO {
    @NotBlank(message = "状态不能为空")
    private String status;              // 状态
    
    private LocalDateTime actualStart;  // 实际开始时间
    private LocalDateTime actualEnd;    // 实际结束时间
}
