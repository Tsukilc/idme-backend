package com.tsukilc.idme.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 位置实体（工厂/车间/产线/工位/仓库）
 * 对应 schema.md 第13节
 * 支持树形结构（自关联）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
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
    private String locationCode;       // 位置编码，必填，唯一
    private String locationName;       // 位置名称，必填
    private Object locationType;       // 位置类型枚举：SDK返回Map结构 {code, cnName, enName, alias}
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    private ObjectReference parentLocation;  // 上级位置（自关联），可选
    
    private String addressText;        // 地址描述
    
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    private ObjectReference manager;   // 负责人（-> Employee），可选
    
    private String remarks;            // 备注
}
