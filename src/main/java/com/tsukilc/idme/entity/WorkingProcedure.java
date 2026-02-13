package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 工序实体
 * 对应 schema.md 第3节
 * 竞赛核心模块（5分）- 支持批量创建和状态更新
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingProcedure {
    // === SDK 系统字段 ===
    private String id;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
    private Integer rdmVersion;
    private Integer rdmDeleteFlag;
    private String rdmExtensionType;
    private String className;
    
    // === 业务字段 ===
    private String procedureCode;           // 工序编号，必填，唯一
    private String procedureName;           // 工序名称，必填
    private String steps;                   // 操作步骤（长文本）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference mainProductionEquipment;  // 主生产设备（-> Equipment）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference mainInspectionEquipment;  // 主检测设备（-> Equipment）
    
    private String operatorUser;            // 操作人员（系统人员）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference operatorRef;    // 操作人员（-> Employee）
    
    private LocalDateTime startTime;        // 开始时间
    private LocalDateTime endTime;          // 结束时间
    private String status;                  // 状态（枚举：未开始/进行中/已完成/暂停）
    private String remarks;                 // 备注
}
