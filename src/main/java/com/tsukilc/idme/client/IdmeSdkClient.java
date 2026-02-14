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
    
    // SDK 要求的固定用户身份（用于避免 modifier 字段不一致问题）
    private static final String SDK_USER = "sysadmin 1";
    
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
        
        log.info("IdmeSdkClient 初始化完成，baseUrl: {}, SDK_USER: {}", idmeConfig.getBaseUrl(), SDK_USER);
    }
    
    /**
     * 创建实体
     * @param entityName 实体名称（如 "Employee"）
     * @param params 创建参数
     * @return 创建后的实体数据
     */
    public <T> T create(String entityName, Object params, Class<T> responseType) {
        String url = buildUrl(entityName, "create");
        // 自动添加 creator 和 modifier 字段
        Map<String, Object> enrichedParams = enrichWithUserFields(params);
        RdmRequest<?> request = RdmRequest.of(enrichedParams);
        
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
        
        // 将params转为Map，过滤掉系统字段（SDK update接口不接受这些字段），并注入 creator/modifier
        Map<String, Object> filteredParams = filterSystemFields(params);
        Map<String, Object> enrichedParams = enrichWithUserFields(filteredParams);
        RdmRequest<?> request = RdmRequest.of(enrichedParams);
        // SDK的update接口返回的data也是数组
        return executeRequestForCreate(url, request, responseType);
    }
    
    /**
     * 过滤掉SDK不接受的系统字段（用于update请求）
     */
    private Map<String, Object> filterSystemFields(Object entity) {
        try {
            // 将entity转为Map
            String json = objectMapper.writeValueAsString(entity);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = objectMapper.readValue(json, Map.class);
            
            // 移除只读系统字段（SDK update接口会拒绝这些字段）
            // creator/modifier 由 enrichWithUserFields 统一注入
            map.remove("createTime");
            map.remove("lastUpdateTime");
            map.remove("rdmDeleteFlag");
            map.remove("rdmExtensionType");
            map.remove("className");
            map.remove("tenant");
            
            return map;
        } catch (Exception e) {
            log.error("过滤系统字段失败: {}", e.getMessage(), e);
            // 如果过滤失败，返回原对象（让SDK处理错误）
            return Map.of("原始对象", entity);
        }
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
        Map<String, Object> enrichedParams = enrichWithUserFields(params);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
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
        Map<String, Object> enrichedParams = enrichWithUserFields(params);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
        // SDK的get接口返回的data也是数组
        return executeRequestForCreate(url, request, responseType);
    }
    
    /**
     * 版本对象检出（Checkout）
     * @param entityName 实体名称（如 Part、WorkingPlan）
     * @param masterId 主对象ID
     * @param workCopyType 工作副本类型（BOTH/ITERATION/VERSION）
     * @return 检出后的工作副本对象
     */
    public <T> T checkout(String entityName, String masterId, String workCopyType, Class<T> responseType) {
        String url = buildUrl(entityName, "checkout");
        Map<String, Object> params = new HashMap<>();
        params.put("masterId", masterId);
        params.put("workCopyType", workCopyType != null ? workCopyType : "BOTH");
        Map<String, Object> enrichedParams = enrichWithUserFields(params);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
        return executeRequestForCreate(url, request, responseType);
    }

    /**
     * 版本对象检入（Checkin）
     * @param entityName 实体名称（如 Part、WorkingPlan）
     * @param masterId 主对象ID
     * @param viewNo 视图号（可选，通常为空字符串）
     * @return 检入后的版本对象
     */
    public <T> T checkin(String entityName, String masterId, String viewNo, Class<T> responseType) {
        String url = buildUrl(entityName, "checkin");
        Map<String, Object> params = new HashMap<>();
        params.put("masterId", masterId);
        params.put("viewNo", viewNo != null ? viewNo : "");
        Map<String, Object> enrichedParams = enrichWithUserFields(params);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
        return executeRequestForCreate(url, request, responseType);
    }

    /**
     * 查询版本历史
     * @param entityName 实体名称
     * @param masterId 主对象ID
     * @return 历史版本列表
     */
    public <T> List<T> getVersionHistory(String entityName, String masterId, Class<T> elementType) {
        // SDK通过list接口查询，condition指定master.id
        String url = buildUrl(entityName, "list") + "?curPage=1&pageSize=1000";
        QueryRequest queryRequest = new QueryRequest();
        Map<String, Object> condition = new HashMap<>();
        condition.put("master.id", masterId);
        queryRequest.setCondition(condition);
        Map<String, Object> enrichedParams = enrichWithUserFields(queryRequest);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
        return executeRequestForList(url, request, elementType);
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

        Map<String, Object> enrichedParams = enrichWithUserFields(queryRequest);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
        return executeRequestForList(url, request, elementType);
    }

    /**
     * 使用find接口查询（支持复杂条件过滤）
     * find接口支持filter条件，可以按嵌套字段过滤
     *
     * @param entityName 实体名称
     * @param filter 过滤条件（格式：{joiner: "and", conditions: [{conditionName: "字段", operator: "=", conditionValues: ["值"]}]}）
     * @param sorts 排序条件（格式：[{sort: "DESC", orderBy: "字段"}]）
     * @param curPage 当前页（从1开始）
     * @param pageSize 每页大小
     * @return 查询结果列表
     */
    public <T> List<T> find(String entityName,
                            Map<String, Object> filter,
                            List<Map<String, String>> sorts,
                            int curPage,
                            int pageSize,
                            Class<T> elementType) {
        String url = buildUrl(entityName, "find") + "/" + pageSize + "/" + curPage;

        // 构建params
        Map<String, Object> params = new HashMap<>();
        if (filter != null) {
            params.put("filter", filter);
        }
        if (sorts != null) {
            params.put("sorts", sorts);
        }
        params.put("isNeedTotal", true);

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("params", params);

        Map<String, Object> enrichedParams = enrichWithUserFields(requestBody);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
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
        
        Map<String, Object> enrichedParams = enrichWithUserFields(queryRequest);
        RdmRequest<Map<String, Object>> request = RdmRequest.of(enrichedParams);
        return executeRequestForList(url, request, elementType);
    }
    
    /**
     * 为 params 注入 creator 和 modifier（SDK 要求，避免版本管理 modifier 不一致问题）
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> enrichWithUserFields(Object params) {
        try {
            Map<String, Object> map = params instanceof Map
                ? (Map<String, Object>) params
                : objectMapper.convertValue(params, Map.class);
            map.put("creator", SDK_USER);
            map.put("modifier", SDK_USER);

            // 统一处理所有日期时间字段（转为Unix时间戳）
            convertDateFieldsToTimestamp(map);

            return map;
        } catch (Exception e) {
            log.warn("enrichWithUserFields 失败，使用原 params: {}", e.getMessage());
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("creator", SDK_USER);
            fallback.put("modifier", SDK_USER);
            if (params instanceof Map) {
                fallback.putAll((Map<String, Object>) params);
            }
            return fallback;
        }
    }

    /**
     * 统一处理所有日期时间字段，转为Unix时间戳（SDK要求）
     * 支持的字段：hireDate, productionDate, startTime, endTime等
     */
    private void convertDateFieldsToTimestamp(Map<String, Object> map) {
        String[] dateFields = {"hireDate", "productionDate", "startTime", "endTime", "plannedStart", "plannedEnd", "actualStart", "actualEnd", "operateTime", "effectiveFrom", "effectiveTo"};

        for (String field : dateFields) {
            if (map.containsKey(field) && map.get(field) != null) {
                Object value = map.get(field);

                if (value instanceof java.time.LocalDateTime) {
                    // LocalDateTime -> Unix时间戳（UTC）
                    java.time.LocalDateTime dt = (java.time.LocalDateTime) value;
                    long timestamp = dt.atZone(java.time.ZoneId.of("UTC")).toInstant().toEpochMilli();
                    map.put(field, timestamp);
                } else if (value instanceof java.time.LocalDate) {
                    // LocalDate -> LocalDateTime -> Unix时间戳（UTC）
                    java.time.LocalDateTime dt = ((java.time.LocalDate) value).atStartOfDay();
                    long timestamp = dt.atZone(java.time.ZoneId.of("UTC")).toInstant().toEpochMilli();
                    map.put(field, timestamp);
                } else if (value instanceof String) {
                    // ISO字符串格式 -> Unix时间戳（UTC）
                    try {
                        java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse((String) value);
                        long timestamp = dateTime.atZone(java.time.ZoneId.of("UTC")).toInstant().toEpochMilli();
                        map.put(field, timestamp);
                    } catch (Exception e) {
                        log.warn("日期字段 {} 转换失败: {}", field, e.getMessage());
                    }
                }
            }
        }
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
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Auth-Token", "idme-api-token")
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
                    // 尝试从响应中提取error_msg字段
                    try {
                        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(responseBody);
                        if (jsonNode.has("error_msg")) {
                            errorMsg += " - " + jsonNode.get("error_msg").asText();
                        }
                        if (jsonNode.has("error_code")) {
                            errorMsg += " (错误码: " + jsonNode.get("error_code").asText() + ")";
                        }
                    } catch (Exception ignored) {}
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
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Auth-Token", "idme-api-token")
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
