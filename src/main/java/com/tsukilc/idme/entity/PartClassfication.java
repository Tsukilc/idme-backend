package com.tsukilc.idme.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物料分类实体
 * SDK实体名称：PartClassfication（注意拼写：少了一个i）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartClassfication {
    private String id;
    private String partClassName;  // 物料分类名称

    // 系统字段
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
    private String rdmExtensionType;
    private Integer rdmDeleteFlag;
    private String className;
    private Long rdmVersion;
}
