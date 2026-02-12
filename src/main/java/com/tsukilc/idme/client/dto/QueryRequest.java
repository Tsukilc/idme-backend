package com.tsukilc.idme.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * iDME SDK 查询请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRequest {
    private Map<String, Object> condition;
    private String orderBy;
    private String orderDirection;  // ASC/DESC
}
