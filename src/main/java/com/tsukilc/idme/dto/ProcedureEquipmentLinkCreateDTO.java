package com.tsukilc.idme.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ProcedureEquipmentLinkCreateDTO {
    @NotBlank(message = "工序不能为空")
    private String procedure;               // 工序ID
    
    @NotBlank(message = "设备不能为空")
    private String equipment1;              // 设备ID
    
    private String role;                    // 角色
    private LocalDateTime plannedStart;
    private LocalDateTime plannedEnd;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private String remarks;
}
