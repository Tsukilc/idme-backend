package com.tsukilc.idme.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 往来单位联系人实体
 * 对应 schema.md 第10节
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerContact {
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
    @JsonDeserialize(using = ObjectReferenceDeserializer.class)
    @JsonSerialize(using = ObjectReferenceSerializer.class)
    private ObjectReference partner;    // 所属往来单位（-> BusinessPartner），必填
    
    private String contactName;         // 联系人姓名，必填
    private String mobile;              // 手机
    private String phone;               // 电话
    private String email;               // 邮箱
    private String role;                // 角色（如：销售/售后/技术支持）
    private String remarks;             // 备注
}
