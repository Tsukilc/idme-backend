package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工序 VO（用于前端展示）
 */
@Data
public class WorkingProcedureVO {
    private String id;
    private String procedureCode;           // 工序编号
    private String procedureName;           // 工序名称
    private String steps;                   // 操作步骤
    
    private String mainProductionEquipment; // 主生产设备ID
    private String mainProductionEquipmentName; // 主生产设备名称（展示用）
    
    private String mainInspectionEquipment; // 主检测设备ID
    private String mainInspectionEquipmentName; // 主检测设备名称（展示用）
    
    private String operatorUser;            // 操作人员（系统人员）
    
    private String operatorRef;             // 操作人员ID
    private String operatorName;            // 操作人员名称（展示用）
    
    private LocalDateTime startTime;        // 开始时间
    private LocalDateTime endTime;          // 结束时间
    private String status;                  // 状态
    private String remarks;                 // 备注
    
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
