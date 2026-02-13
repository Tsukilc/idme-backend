package com.tsukilc.idme.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * WorkingStateRef反序列化器
 * 支持两种格式：
 * 1. String枚举值（DTO输入）
 * 2. Object {code, cnName, enName, alias}（SDK响应）
 */
public class WorkingStateRefDeserializer extends JsonDeserializer<WorkingStateRef> {
    @Override
    public WorkingStateRef deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        if (node.isTextual()) {
            // 格式1: "CHECKED_IN"（来自DTO）
            return new WorkingStateRef(node.asText());
        } else if (node.isObject()) {
            // 格式2: {code, cnName, enName, alias}（来自SDK）
            String code = node.has("code") ? node.get("code").asText() : null;
            String cnName = node.has("cnName") ? node.get("cnName").asText() : null;
            String enName = node.has("enName") ? node.get("enName").asText() : null;
            String alias = node.has("alias") ? node.get("alias").asText() : null;
            return new WorkingStateRef(code, cnName, enName, alias);
        } else if (node.isNull()) {
            return null;
        }
        
        throw new IOException("Invalid workingState format: " + node);
    }
}
