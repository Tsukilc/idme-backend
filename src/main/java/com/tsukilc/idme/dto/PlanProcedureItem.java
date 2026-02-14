package com.tsukilc.idme.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 工艺-工序关联项（用于查询工艺路线下的工序列表）
 * 包含关联信息 + 工序详细信息
 */
@Data
public class PlanProcedureItem {
    // === 关联信息（来自PlanProcedureLink） ===
    private String linkId;                  // 关联ID
    private Integer sequence;               // 工序顺序
    private BigDecimal standardDurationMin; // 标准工时（分钟）

    // === 工序详细信息（来自WorkingProcedure） ===
    private String procedureId;             // 工序ID
    private String procedureCode;           // 工序编号
    private String procedureName;           // 工序名称
    private String steps;                   // 操作步骤
    private String status;                  // 状态
    private String mainProductionEquipment; // 主生产设备ID
    private String mainInspectionEquipment; // 主检测设备ID
    private String operatorRef;             // 操作人员ID
    private String remarks;                 // 备注
}
