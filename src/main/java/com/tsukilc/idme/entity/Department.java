package com.tsukilc.idme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 部门实体（内部使用，包含完整SDK字段）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    // 业务字段
    private String id;
    private String deptCode;
    private String deptName;
    private ObjectReference parentDept;  // 父部门引用
    private String manager;
    private String remarks;
    
    // 系统字段
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
    private Integer rdmVersion;
    private Integer rdmDeleteFlag;
    private String rdmExtensionType;
    private String className;
}
