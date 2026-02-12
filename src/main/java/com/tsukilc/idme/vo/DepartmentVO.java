package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 部门 VO（返回给前端）
 * 严格参照 docs/openapi.yaml 的 Department schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVO {
    private String id;
    private String deptCode;
    private String deptName;
    private String parentDept;  // 父部门ID（扁平化）
    private String manager;
    private String remarks;
    private List<DepartmentVO> children;  // 子部门列表（用于树形结构）
}
