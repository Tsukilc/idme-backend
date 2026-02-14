package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工序 VO（严格遵循openapi.yaml的WorkingProcedure schema）
 */
@Data
public class WorkingProcedureVO {
    private String id;
    private String procedureCode;           // 工序编号
    private String procedureName;           // 工序名称
    private String steps;                   // 操作步骤
    private String mainProductionEquipment; // 主生产设备ID
    private String mainInspectionEquipment; // 主检测设备ID
    private String operatorUser;            // 操作人员（系统人员）
    private String operatorRef;             // 操作人员ID
    private LocalDateTime startTime;        // 开始时间
    private LocalDateTime endTime;          // 结束时间
    private String status;                  // 状态
    private String remarks;                 // 备注
}
