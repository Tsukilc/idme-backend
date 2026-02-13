package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工艺路线 VO（用于前端展示）
 */
@Data
public class WorkingPlanVO {
    private String id;
    private String masterId;                // 主对象ID
    private String planCode;                // 工艺编号
    private String planName;                // 工艺名称
    private String businessVersion;         // 业务版本号
    private String version;                 // 系统版本号（如"A.1"）
    
    private String productPart;             // 所属产品/零件ID
    private String productPartName;         // 所属产品/零件名称（展示用）
    
    private String description;             // 工艺描述
    private String operatorUser;            // 操作人员（系统人员）
    
    private String operatorRef;             // 操作人员ID
    private String operatorName;            // 操作人员名称（展示用）
    
    private LocalDateTime operateTime;      // 操作时间
    private Map<String, Object> equipmentUsage;  // 设备使用情况
    private String status;                  // 状态
    private String remarks;                 // 备注
    
    private Boolean latest;                 // 是否最新有效版本
    private Boolean workingCopy;            // 是否工作副本
    
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
