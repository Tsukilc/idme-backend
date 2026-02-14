package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工序-物料关联 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedurePartLinkVO {
    private String id;
    private String procedure;  // 工序ID（从source提取）
    private String part;  // 物料ID（从target提取）
    private String name;  // 名称
    private String description;  // 描述
    private Integer quantity;  // 数量
    private String uom;  // 计量单位ID
    private Boolean isMandatory;  // 是否必需
    private String role;  // 角色
}
