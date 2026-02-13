package com.tsukilc.idme.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BOM项 VO（用于前端展示）
 */
@Data
public class BOMItemVO {
    private String id;
    
    private String parentPart;              // 父项物料ID
    private String parentPartName;          // 父项物料名称（展示用）
    
    private String childPart;               // 子项物料ID
    private String childPartName;           // 子项物料名称（展示用）
    
    private BigDecimal quantity;            // 用量
    private String uom;                     // 单位
    private Integer findNumber;             // 项次
    private LocalDate effectiveFrom;        // 生效时间
    private LocalDate effectiveTo;          // 失效时间
    private String remarks;                 // 备注
    
    // 树形结构支持
    private List<BOMItemVO> children;       // 子BOM项列表（树形查询用）
    
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
