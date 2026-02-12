package com.tsukilc.idme.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 统一响应包装类
 * 参照 docs/openapi.yaml 的 ApiResponse schema
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String result;  // SUCCESS/FAIL
    private T data;
    private List<String> errors;
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data, Collections.emptyList());
    }
    
    public static <T> ApiResponse<T> fail(String... errors) {
        return new ApiResponse<>("FAIL", null, Arrays.asList(errors));
    }
}
