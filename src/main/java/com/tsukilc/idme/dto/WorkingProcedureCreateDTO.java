package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 工序创建/更新 DTO
 */
@Data
public class WorkingProcedureCreateDTO {
    @NotBlank(message = "工序编号不能为空")
    private String procedureCode;           // 工序编号，必填，唯一
    
    @NotBlank(message = "工序名称不能为空")
    private String procedureName;           // 工序名称，必填
    
    private String steps;                   // 操作步骤（长文本）
    
    @NotBlank(message = "主生产设备不能为空")
    private String mainProductionEquipment; // 主生产设备ID（-> Equipment），SDK必填
    private String mainInspectionEquipment; // 主检测设备ID（-> Equipment）
    private String operatorUser;            // 操作人员（系统人员）
    private String operatorRef;             // 操作人员ID（-> Employee）
    private LocalDate startTime;            // 开始时间（前端传LocalDate，Service转为LocalDateTime）
    private LocalDate endTime;              // 结束时间（前端传LocalDate，Service转为LocalDateTime）
    private String status;                  // 状态
    private String remarks;                 // 备注
}
