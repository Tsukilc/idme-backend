package com.tsukilc.idme.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsukilc.idme.client.dto.QueryRequest;
import com.tsukilc.idme.client.dto.RdmRequest;
import com.tsukilc.idme.client.dto.RdmResponse;
import com.tsukilc.idme.config.IdmeConfig;
import com.tsukilc.idme.exception.IdmeException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * iDME SDK HTTP 客户端封装
 * 负责所有与 iDME SDK 的 HTTP 通信
 */
@Component
@Slf4j
public class IdmeSdkClient {
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final IdmeConfig idmeConfig;
    private final String authHeader;
    
    @Autowired
    public IdmeSdkClient(IdmeConfig idmeConfig, ObjectMapper objectMapper) {
        this.idmeConfig = idmeConfig;
        this.objectMapper = objectMapper;
        
        // 初始化 OkHttp 客户端
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(idmeConfig.getTimeout(), TimeUnit.MILLISECONDS)
            .readTimeout(idmeConfig.getTimeout(), TimeUnit.MILLISECONDS)
            .writeTimeout(idmeConfig.getTimeout(), TimeUnit.MILLISECONDS)
            .build();
        
        // 初始化 Basic Auth 认证头
        String credentials = idmeConfig.getUsername() + ":" + idmeConfig.getPassword();
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        
        log.info("IdmeSdkClient 初始化完成，baseUrl: {}", idmeConfig.getBaseUrl());
    }
    
    /**
     * 创建实体
     * @param entityName 实体名称（如 "Employee"）
     * @param params 创建参数
     * @return 创建后的实体数据
     */
    public <T> T create(String entityName, Object params, Class<T> responseType) {
        String url = buildUrl(entityName, "create");
        RdmRequest<?> request = RdmRequest.of(params);
        
        // SDK的create接口返回的data是数组，需要特殊处理
        return executeRequestForCreate(url, request, responseType);
    }
    
    /**
     * 更新实体
     * @param entityName 实体名称
     * @param params 更新参数（需包含 id）
     * @return 更新后的实体数据
     */
    public <T> T update(String entityName, Object params, Class<T> responseType) {
        String url = buildUrl(entityName, "update");
        RdmRequest<?> request = RdmRequest.of(params);
        // SDK的update接口返回的data也是数组
        return executeRequestForCreate(url, request, responseType);
    }
    
    /**
     * 删除实体
     * @param entityName 实体名称
     * @param id 实体ID
     */
    public void delete(String entityName, String id) {
        String url = buildUrl(entityName, "delete");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(params);
        executeRequest(url, request, Void.class);
    }
    
    /**
     * 根据ID查询单个实体
     * @param entityName 实体名称
     * @param id 实体ID
     * @return 实体数据
     */
    public <T> T get(String entityName, String id, Class<T> responseType) {
        String url = buildUrl(entityName, "get");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(params);
        // SDK的get接口返回的data也是数组
        return executeRequestForCreate(url, request, responseType);
    }
    
    /**
     * 分页查询（使用list接口，返回完整的基础属性数据）
     * list接口返回完整的基础属性，无需手动指定字段
     * 
     * @param entityName 实体名称
     * @param queryRequest 查询条件
     * @param curPage 当前页（从1开始）
     * @param pageSize 每页大小
     * @return 查询结果列表
     */
    public <T> List<T> list(String entityName, QueryRequest queryRequest, 
                            int curPage, int pageSize, Class<T> elementType) {
        String url = buildUrl(entityName, "list") 
            + "?curPage=" + curPage + "&pageSize=" + pageSize;
        
        // 如果没有查询条件，创建空条件
        if (queryRequest == null) {
            queryRequest = new QueryRequest();
            queryRequest.setCondition(new HashMap<>());
        }
        
        RdmRequest<QueryRequest> request = RdmRequest.of(queryRequest);
        return executeRequestForList(url, request, elementType);
    }
    
    /**
     * 分页查询（仅返回系统字段）
     * 注意：SDK的query接口默认只返回系统字段，不返回业务字段
     * @deprecated 建议使用list接口获取完整数据
     */
    @Deprecated
    public <T> List<T> query(String entityName, QueryRequest queryRequest, 
                             int curPage, int pageSize, Class<T> elementType) {
        String url = buildUrl(entityName, "query") 
            + "?curPage=" + curPage + "&pageSize=" + pageSize;
        
        // 如果没有查询条件，创建空条件
        if (queryRequest == null) {
            queryRequest = new QueryRequest();
            queryRequest.setCondition(new HashMap<>());
        }
        
        RdmRequest<QueryRequest> request = RdmRequest.of(queryRequest);
        return executeRequestForList(url, request, elementType);
    }
    
    /**
     * 构建 URL
     */
    private String buildUrl(String entityName, String operation) {
        return idmeConfig.getBaseUrl() + "/dynamic/api/" + entityName + "/" + operation;
    }
    
    /**
     * 执行 HTTP 请求（通用）
     */
    private <T> T executeRequest(String url, RdmRequest<?> request, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            
            Request httpRequest = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", authHeader)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Auth-Token", "idme-api-token")  // iDME SDK 必需的认证头
                .build();
            
            log.debug("发送请求: {} - {}", url, jsonBody);
            
            try (Response response = httpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                log.debug("收到响应: {} - {}", response.code(), responseBody);
                
                // #region agent log
                try{java.nio.file.Files.write(java.nio.file.Paths.get("/Users/zbj/IdeaProjects/idme/.cursor/debug.log"),("{\"id\":\"log_"+System.currentTimeMillis()+"_1\",\"timestamp\":"+System.currentTimeMillis()+",\"location\":\"IdmeSdkClient.java:152\",\"message\":\"SDK响应状态码\",\"data\":{\"httpCode\":"+response.code()+",\"isSuccessful\":"+response.isSuccessful()+"},\"hypothesisId\":\"A\"}"+System.lineSeparator()).getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);}catch(Exception e){}
                // #endregion
                
                if (!response.isSuccessful()) {
                    throw new IdmeException("HTTP 请求失败: " + response.code() + " - " + responseBody);
                }
                
                log.info("SDK 原始响应: {}", responseBody);
                
                // #region agent log
                try{java.nio.file.Files.write(java.nio.file.Paths.get("/Users/zbj/IdeaProjects/idme/.cursor/debug.log"),("{\"id\":\"log_"+System.currentTimeMillis()+"_2\",\"timestamp\":"+System.currentTimeMillis()+",\"location\":\"IdmeSdkClient.java:162\",\"message\":\"SDK原始响应内容\",\"data\":{\"responseLength\":"+responseBody.length()+",\"responsePreview\":\""+responseBody.substring(0,Math.min(200,responseBody.length())).replace("\"","\\\"").replace("\n","\\\\n")+"\"},\"hypothesisId\":\"A,B\",\"runId\":\"post-fix\"}"+System.lineSeparator()).getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);}catch(Exception e){}
                // #endregion
                
                // 解析响应
                if (responseType == Void.class || responseType == void.class) {
                    return null;
                }
                
                // 首先检查是否是错误响应
                RdmResponse<T> rdmResponse = objectMapper.readValue(responseBody, 
                    objectMapper.getTypeFactory().constructParametricType(RdmResponse.class, responseType));
                
                // #region agent log
                try{java.nio.file.Files.write(java.nio.file.Paths.get("/Users/zbj/IdeaProjects/idme/.cursor/debug.log"),("{\"id\":\"log_"+System.currentTimeMillis()+"_3\",\"timestamp\":"+System.currentTimeMillis()+",\"location\":\"IdmeSdkClient.java:172\",\"message\":\"解析RdmResponse\",\"data\":{\"result\":\""+rdmResponse.getResult()+"\",\"dataIsNull\":"+(rdmResponse.getData()==null)+",\"hasErrors\":"+(rdmResponse.getErrors()!=null&&!rdmResponse.getErrors().isEmpty())+"},\"hypothesisId\":\"A,E\",\"runId\":\"post-fix\"}"+System.lineSeparator()).getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);}catch(Exception e){}
                // #endregion
                
                if (!"SUCCESS".equals(rdmResponse.getResult())) {
                    String errorMsg = "SDK 调用失败";
                    if (rdmResponse.getErrors() != null && !rdmResponse.getErrors().isEmpty()) {
                        errorMsg += ": " + String.join(", ", rdmResponse.getErrors());
                    }
                    // 尝试从响应中提取error_msg字段
                    try {
                        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(responseBody);
                        if (jsonNode.has("error_msg")) {
                            errorMsg += " - " + jsonNode.get("error_msg").asText();
                        }
                    } catch (Exception ignored) {}
                    
                    throw new IdmeException(errorMsg);
                }
                
                // #region agent log
                try{java.nio.file.Files.write(java.nio.file.Paths.get("/Users/zbj/IdeaProjects/idme/.cursor/debug.log"),("{\"id\":\"log_"+System.currentTimeMillis()+"_4\",\"timestamp\":"+System.currentTimeMillis()+",\"location\":\"IdmeSdkClient.java:195\",\"message\":\"返回成功数据\",\"data\":{\"dataIsNull\":"+(rdmResponse.getData()==null)+"},\"hypothesisId\":\"A,B\",\"runId\":\"post-fix\"}"+System.lineSeparator()).getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);}catch(Exception e){}
                // #endregion
                
                return rdmResponse.getData();
                
            }
        } catch (IOException e) {
            log.error("请求失败: {} - {}", url, e.getMessage(), e);
            throw new IdmeException("SDK 调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 执行 HTTP 请求（专门处理create接口返回数组的情况）
     */
    private <T> T executeRequestForCreate(String url, RdmRequest<?> request, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            
            Request httpRequest = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", authHeader)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Auth-Token", "idme-api-token")
                .build();
            
            log.debug("发送创建请求: {} - {}", url, jsonBody);
            
            try (Response response = httpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                log.debug("收到创建响应: {} - {}", response.code(), responseBody);
                
                if (!response.isSuccessful()) {
                    throw new IdmeException("HTTP 请求失败: " + response.code() + " - " + responseBody);
                }
                
                log.info("SDK 创建原始响应: {}", responseBody);
                
                // create接口返回的data是数组，需要解析为List
                JavaType listType = objectMapper.getTypeFactory()
                    .constructParametricType(List.class, responseType);
                JavaType wrapperType = objectMapper.getTypeFactory()
                    .constructParametricType(RdmResponse.class, listType);
                
                RdmResponse<List<T>> rdmResponse = objectMapper.readValue(responseBody, wrapperType);
                
                if (!"SUCCESS".equals(rdmResponse.getResult())) {
                    String errorMsg = "SDK 调用失败";
                    if (rdmResponse.getErrors() != null && !rdmResponse.getErrors().isEmpty()) {
                        errorMsg += ": " + String.join(", ", rdmResponse.getErrors());
                    }
                    throw new IdmeException(errorMsg);
                }
                
                List<T> resultList = rdmResponse.getData();
                if (resultList == null || resultList.isEmpty()) {
                    throw new IdmeException("创建实体失败，SDK返回空数据");
                }
                
                return resultList.get(0);  // 返回数组的第一个元素
                
            }
        } catch (IOException e) {
            log.error("创建请求失败: {} - {}", url, e.getMessage(), e);
            throw new IdmeException("SDK 调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 执行 HTTP 请求（返回列表）
     */
    private <T> List<T> executeRequestForList(String url, RdmRequest<?> request, Class<T> elementType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            
            Request httpRequest = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", authHeader)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Auth-Token", "idme-api-token")  // iDME SDK 必需的认证头
                .build();
            
            log.debug("发送查询请求: {} - {}", url, jsonBody);
            
            try (Response response = httpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                log.debug("收到查询响应: {} - {}", response.code(), responseBody);
                
                if (!response.isSuccessful()) {
                    throw new IdmeException("HTTP 请求失败: " + response.code() + " - " + responseBody);
                }
                
                log.info("SDK 查询原始响应: {}", responseBody);
                
                // 查询接口返回的data是数组，需要解析为 RdmResponse<List<T>>
                JavaType listType = objectMapper.getTypeFactory()
                    .constructParametricType(List.class, elementType);
                JavaType wrapperType = objectMapper.getTypeFactory()
                    .constructParametricType(RdmResponse.class, listType);
                
                RdmResponse<List<T>> rdmResponse = objectMapper.readValue(responseBody, wrapperType);
                
                if (!"SUCCESS".equals(rdmResponse.getResult())) {
                    String errorMsg = "SDK 查询失败";
                    if (rdmResponse.getErrors() != null && !rdmResponse.getErrors().isEmpty()) {
                        errorMsg += ": " + String.join(", ", rdmResponse.getErrors());
                    }
                    throw new IdmeException(errorMsg);
                }
                
                return rdmResponse.getData();
                
            }
        } catch (IOException e) {
            log.error("查询请求失败: {} - {}", url, e.getMessage(), e);
            throw new IdmeException("SDK 查询失败: " + e.getMessage(), e);
        }
    }
}
