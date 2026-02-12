package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 往来单位 VO（返回给前端）
 * 严格参照 openapi.yaml 的 BusinessPartner schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPartnerVO {
    private String id;
    private String partnerCode;   // 往来单位编码
    private String partnerName;   // 单位名称
    private String partnerType;   // 单位类型
    private String phone;         // 电话
    private String email;         // 邮箱
    private String website;       // 官网URL
    private String addressText;   // 地址
    private Object extra;         // 扩展信息
}
