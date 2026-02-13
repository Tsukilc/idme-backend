package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 联系人创建/更新 DTO
 */
@Data
public class PartnerContactCreateDTO {
    @NotBlank(message = "所属往来单位不能为空")
    private String partner;  // 往来单位ID（-> BusinessPartner）
    
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;
    
    private String mobile;
    private String phone;
    private String email;
    private String role;  // 角色（如：销售/售后/技术支持）
    private String remarks;
}
