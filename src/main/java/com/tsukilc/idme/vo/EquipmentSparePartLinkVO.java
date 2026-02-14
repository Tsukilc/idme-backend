package com.tsukilc.idme.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备-备件关联 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentSparePartLinkVO {
    private String id;
    private String equipment;  // 设备ID
    private String sparePart;  // 备件/物料ID
    private String unit;  // 计量单位ID
    private Integer quantity;  // 数量
    private Integer replacementCycleDays;  // 更换周期（天数）
    private Boolean isCritical;  // 是否关键
    private String remarks;  // 备注
}
