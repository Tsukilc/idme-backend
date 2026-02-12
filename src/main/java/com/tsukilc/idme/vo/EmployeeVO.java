package com.tsukilc.idme.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

/**
 * Employee VO - 返回给前端
 * 严格参照 docs/openapi.yaml 的 Employee schema
 * 不包含系统字段（creator, modifier, createTime 等）
 */
@Data
public class EmployeeVO {
    private String id;
    private String employeeNo;
    private String employeeName;
    private String userRef;
    private String jobTitle;
    private String dept;  // 部门ID（扁平化）
    private String phone;
    private String email;
    private String status;  // 在职/离职/外协
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    
    private Map<String, Object> extra;
}
