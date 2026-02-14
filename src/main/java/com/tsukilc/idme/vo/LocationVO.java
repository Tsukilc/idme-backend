package com.tsukilc.idme.vo;

import lombok.Data;

import java.util.List;

/**
 * 位置响应 VO（严格遵循openapi.yaml的Location schema）
 */
@Data
public class LocationVO {
    private String id;
    private String locationCode;
    private String locationName;
    private String locationType;
    private String parentLocation;     // 上级位置ID
    private String addressText;
    private String manager;            // 负责人ID
    private String remarks;
    private List<LocationVO> children; // 子位置（用于树形结构）
}
