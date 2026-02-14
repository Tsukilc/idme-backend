package com.tsukilc.idme.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备分类 VO
 */
@Data
public class EquipmentClassficationVO {
    private String id;
    private String equipmentClassName;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
