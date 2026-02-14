package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工序-设备关联 VO（用于前端展示）
 */
@Data
public class ProcedureEquipmentLinkVO {
    private String id;

    private String procedure;               // 工序ID
    private String procedureName;           // 工序名称（展示用）

    private String equipment1;              // 设备ID
    private String equipmentName;           // 设备名称（展示用）

    private String role;                    // 角色
    private LocalDateTime plannedStart;     // 计划开始
    private LocalDateTime plannedEnd;       // 计划结束
    private LocalDateTime actualStart;      // 实际开始
    private LocalDateTime actualEnd;        // 实际结束
    private String remarks;                 // 备注

    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
