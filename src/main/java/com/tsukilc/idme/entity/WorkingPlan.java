package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工艺路线实体（VersionObject）
 * 对应 schema.md 第4节
 * 竞赛核心模块（10分）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingPlan {
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
    
    // === 版本对象字段 ===
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference master;         // 主对象引用
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference branch;         // 分支对象引用
    
    private Boolean latest;                 // 是否最新有效版本
    private Boolean latestIteration;        // 是否最新迭代
    private Integer versionCode;            // 版本内码
    private Integer iteration;              // 迭代版本
    private String version;                 // 版本号（系统生成，如"A.1"）
    private Boolean latestVersion;          // 是否最新版本
    private Boolean workingCopy;            // 是否工作副本
    private WorkingStateRef workingState;   // 工作状态
    
    // === 业务字段 ===
    private String planCode;                // 工艺编号，必填，唯一
    private String planName;                // 工艺名称，必填
    private String businessVersion;         // 业务版本号（如"1.0"）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference productPart;    // 所属产品/零件（-> Part）
    
    private String description;             // 工艺描述（评分会看）
    private String operatorUser;            // 操作人员（系统人员）
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference operatorRef;    // 操作人员（-> Employee）
    
    private LocalDateTime operateTime;      // 操作时间
    private Map<String, Object> equipmentUsage;  // 设备使用情况（JSON）
    private String status;                  // 状态（Draft/Released/Obsolete）
    private String remarks;                 // 备注
}
