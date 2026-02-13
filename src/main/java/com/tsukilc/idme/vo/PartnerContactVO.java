package com.tsukilc.idme.vo;

import lombok.Data;

/**
 * 联系人响应 VO
 */
@Data
public class PartnerContactVO {
    private String id;
    private String partner;         // 往来单位ID
    private String partnerName;     // 往来单位名称（便于前端展示）
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String role;
    private String remarks;
}
