package com.tsukilc.idme.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * PartnerTypeRef 反序列化器
 * 支持从String或Object反序列化
 */
public class PartnerTypeRefDeserializer extends JsonDeserializer<PartnerTypeRef> {
    
    @Override
    public PartnerTypeRef deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        if (node == null || node.isNull()) {
            return null;
        }
        
        if (node.isTextual()) {
            // String类型：直接设置enName
            return new PartnerTypeRef(node.asText());
        }
        
        if (node.isObject()) {
            // Object类型：提取所有字段
            PartnerTypeRef ref = new PartnerTypeRef();
            if (node.has("code")) {
                ref.setCode(node.get("code").asText());
            }
            if (node.has("cnName")) {
                ref.setCnName(node.get("cnName").asText());
            }
            if (node.has("enName")) {
                ref.setEnName(node.get("enName").asText());
            }
            if (node.has("alias")) {
                ref.setAlias(node.get("alias").asText());
            }
            return ref;
        }
        
        return null;
    }
}
