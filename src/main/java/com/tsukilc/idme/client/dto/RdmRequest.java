package com.tsukilc.idme.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iDME SDK 请求包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RdmRequest<T> {
    private T params;
    private String applicationId;
    
    public static <T> RdmRequest<T> of(T params) {
        return new RdmRequest<>(params, null);
    }
}
