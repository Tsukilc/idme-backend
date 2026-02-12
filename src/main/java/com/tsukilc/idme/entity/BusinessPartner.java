package com.tsukilc.idme.entity;

import com.tsukilc.idme.entity.ObjectReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 往来单位实体（供应商、生产厂家、服务商等）
 * 对应 schema.md 第9节
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPartner {
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
    private String partnerCode;   // 往来单位编码，必填，唯一
    private String partnerName;   // 单位名称，必填
    private PartnerTypeRef partnerType;   // 单位类型（SDK返回对象，create时传String）
    private String phone;         // 电话
    private String email;         // 邮箱
    private String website;       // 官网URL
    private String addressText;   // 地址
    private Object extra;         // 扩展信息（JSON）
}
