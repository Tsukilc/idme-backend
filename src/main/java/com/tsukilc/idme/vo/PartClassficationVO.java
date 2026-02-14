package com.tsukilc.idme.vo;

import lombok.Data;

/**
 * 物料分类 VO（严格遵循openapi.yaml）
 */
@Data
public class PartClassficationVO {
    private String id;
    private String partClassName;  // 物料分类名称
}
