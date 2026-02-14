package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Employee 内部实体
 * 对应 SDK 中的 EmployeeViewDTO，包含所有字段
 */
@Data
public class Employee {
    // 业务字段
    private String id;
    private String employeeNo;
    private String employeeName;
    private ObjectReference dept;  // 参考对象
    private String jobTitle;
    private Object status;  // 临时改为Object，查看SDK返回的实际结构
    private String phone;
    private String email;

    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")  // 去掉自定义格式，使用默认反序列化器
    private LocalDateTime hireDate;

    private String userRef;
    private Map<String, Object> extra;
    
    // 系统字段（从 SDK 返回，内部使用，不暴露给前端）
    private String creator;
    private String modifier;

    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")  // 去掉自定义格式，使用默认反序列化器
    private LocalDateTime createTime;

    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")  // 去掉自定义格式，使用默认反序列化器
    private LocalDateTime lastUpdateTime;
    
    private Integer rdmVersion;
    private Integer rdmDeleteFlag;
    private String rdmExtensionType;
    private String className;
}
