package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量单位 VO（返回给前端）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitVO {
    private String id;
    private String unitName;           // 单位名称
    private String unitDisplayName;    // 显示名称
    private String unitCategory;       // 单位分类
    private String unitFactor;         // 单位因子
    private String mesurementSystem;   // 测量系统
}
