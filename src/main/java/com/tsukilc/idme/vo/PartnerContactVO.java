package com.tsukilc.idme.vo;

import lombok.Data;

/**
 * 联系人响应 VO（严格遵循openapi.yaml的PartnerContact schema）
 */
@Data
public class PartnerContactVO {
    private String id;
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String role;
    private String remarks;
}
