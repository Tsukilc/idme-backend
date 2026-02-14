package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 工序-设备关联实体
 * 对应 schema.md 第7节
 * 支持双向查询
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureEquipmentLink {
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
    // SDK同时使用source/target（关系对象标准字段）和procedure/equipment1（业务别名）
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference source;         // 来源（-> WorkingProcedure），SDK标准字段

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference target;         // 目标（-> Equipment），SDK标准字段

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference procedure;      // 工序（-> WorkingProcedure），业务别名

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference equipment1;     // 设备（-> Equipment），业务别名

    private Object role;                    // 角色（SDK返回Map结构 {code, cnName, enName, alias}）
    private LocalDateTime plannedStart;     // 计划开始
    private LocalDateTime plannedEnd;       // 计划结束
    private LocalDateTime actualStart;      // 实际开始
    private LocalDateTime actualEnd;        // 实际结束
    private String remarks;                 // 备注
}
