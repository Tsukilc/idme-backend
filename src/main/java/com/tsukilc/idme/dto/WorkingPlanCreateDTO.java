package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Map;

/**
 * 工艺路线创建/更新 DTO
 */
@Data
public class WorkingPlanCreateDTO {
    @NotBlank(message = "工艺编号不能为空")
    private String planCode;                // 工艺编号，必填，唯一
    
    @NotBlank(message = "工艺名称不能为空")
    private String planName;                // 工艺名称，必填
    
    private String businessVersion;         // 业务版本号（如"1.0"）
    private String productPart;             // 所属产品/零件ID（-> Part）
    private String description;             // 工艺描述
    private String operatorUser;            // 操作人员（系统人员）
    private String operatorRef;             // 操作人员ID（-> Employee）
    private LocalDate operateTime;          // 操作时间（日期格式，如2026-12-12）
    private Map<String, Object> equipmentUsage;  // 设备使用情况（JSON）
    private String status;                  // 状态（Draft/Released/Obsolete）
    private String remarks;                 // 备注
}
