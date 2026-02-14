package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计划-工序关联 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanProcedureLinkVO {
    private String id;
    private String plan;  // 计划ID（从source提取）
    private String procedure;  // 工序ID（从target提取）
    private String name;  // 名称
    private String description;  // 描述
    private Double standardDurationMin;  // 标准时长（分钟）
    private Integer sequenceNo;  // 序号
    private String requirement;  // 要求
}
