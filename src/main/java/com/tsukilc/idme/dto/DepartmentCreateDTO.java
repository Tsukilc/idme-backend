package com.tsukilc.idme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门创建/更新 DTO
 * 严格参照 docs/openapi.yaml 的 DepartmentCreate schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateDTO {
    private String deptCode;
    private String deptName;
    private String parentDept;  // 父部门ID（可选）
    private String manager;
    private String remarks;
}
