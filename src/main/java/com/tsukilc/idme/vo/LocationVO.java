package com.tsukilc.idme.vo;

import lombok.Data;

/**
 * 位置响应 VO
 */
@Data
public class LocationVO {
    private String id;
    private String locationCode;
    private String locationName;
    private String locationType;
    private String parentLocation;     // 上级位置ID
    private String parentLocationName; // 上级位置名称（便于前端展示）
    private String addressText;
    private String manager;            // 负责人ID
    private String managerName;        // 负责人姓名（便于前端展示）
    private String remarks;
}
