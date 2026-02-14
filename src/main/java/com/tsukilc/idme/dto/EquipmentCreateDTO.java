package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 设备创建/更新 DTO
 */
@Data
public class EquipmentCreateDTO {
    @NotBlank(message = "设备编码不能为空")
    private String equipmentCode;           // 设备编码，必填，唯一
    
    @NotBlank(message = "设备名称不能为空")
    private String equipmentName;           // 设备名称，必填
    
    private String manufacturerName;        // 生产厂家ID（-> BusinessPartner）
    private String brand;                   // 品牌
    private String modelSpec;               // 规格型号
    private String equipmentModelRef;       // 设备机型ID（-> EquipmentModel）
    
    @NotBlank(message = "供应商不能为空")
    private String supplierName;            // 供应商ID（-> BusinessPartner），SDK必填
    private LocalDateTime productionDate;   // 生产日期（SDK使用时间戳）
    private Integer serviceLifeYears;       // 使用年限
    private String depreciationMethod;      // 折旧方式
    private String locationText;            // 位置文本
    private String locationRef;             // 位置引用ID（-> Location）
    private String status;                  // 设备状态
    private String serialNumber;            // 序列号
    private String category;                // 设备分类ID（-> EquipmentClassfication，可选）
    private Object techParams;              // 技术参数（SDK接受数组或对象）
    private String remarks;                 // 备注
}
