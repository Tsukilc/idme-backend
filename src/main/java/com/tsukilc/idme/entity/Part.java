package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物料实体（版本对象）
 * 对应 SDK 的 PartViewDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Part {
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
    private ObjectReference master;  // 主对象引用（-> PartMaster）

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference branch;  // 分支对象引用（-> PartBranch）

    private Boolean latest;  // 是否最新有效版本
    private Boolean latestIteration;  // 是否最新迭代
    private Integer versionCode;  // 版本内码
    private Integer iteration;  // 迭代版本
    private String version;  // 版本号（系统生成，如"A.1"）
    private Boolean latestVersion;  // 是否最新版本
    private Boolean workingCopy;  // 是否工作副本（已检出）
    private Object workingState;  // 工作状态（SDK返回Map结构 {code, cnName, enName, alias}）
    private LocalDateTime checkOutTime;  // 检出时间
    private String checkOutUserName;  // 检出用户名
    private String preVersionId;  // 前一版本ID
    private String securityLevel;  // 安全级别（如"internal"）
    
    // === 业务字段 ===
    private String partNumber;  // 物料编号
    private String partName;  // 物料名称
    private String modelSpec;  // 规格型号

    private Integer stockQty;  // 库存数量

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference unit;  // 计量单位（-> Unit）

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference supplierName;  // 供应商（-> BusinessPartner）

    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectReference category;  // 物料分类（分类对象）

    private String businessVersion;  // 业务版本号（展示用，如"1.0"）
    private String description;  // 描述
    private String drawingUrl;  // 图纸链接
    private Object extra;  // 扩展信息（JSON）
}
