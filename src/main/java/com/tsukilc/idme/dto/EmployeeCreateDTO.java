package com.tsukilc.idme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

/**
 * Employee 创建/更新 DTO
 * 严格参照 docs/openapi.yaml 的 EmployeeCreate schema
 */
@Data
public class EmployeeCreateDTO {
    
    @NotBlank(message = "员工编号不能为空")
    private String employeeNo;
    
    @NotBlank(message = "员工姓名不能为空")
    private String employeeName;
    
    @NotBlank(message = "部门不能为空")
    private String dept;  // 部门ID（前端传字符串，SDK要求必填）
    
    private String userRef;
    private String jobTitle;
    private String phone;
    private String email;
    private String status;  // 在职/离职/外协
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    
    private Map<String, Object> extra;
}
