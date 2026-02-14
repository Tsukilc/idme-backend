package com.tsukilc.idme.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工序状态更新 DTO（更新实际开始/结束时间）
 */
@Data
public class ProcedureStatusUpdateDTO {
    private LocalDateTime actualStart;  // 实际开始时间
    private LocalDateTime actualEnd;    // 实际结束时间
}
