package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 往来单位创建/更新 DTO
 * 严格参照 openapi.yaml 的 BusinessPartnerCreate schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPartnerCreateDTO {
    @NotBlank(message = "往来单位编码不能为空")
    private String partnerCode;   // 往来单位编码
    
    @NotBlank(message = "单位名称不能为空")
    private String partnerName;   // 单位名称
    
    private String partnerType;   // 单位类型：Manufacturer/Supplier/ServiceProvider/Customer
    private String phone;         // 电话
    private String email;         // 邮箱
    private String website;       // 官网URL
    private String addressText;   // 地址
    private Object extra;         // 扩展信息（JSON）
}
