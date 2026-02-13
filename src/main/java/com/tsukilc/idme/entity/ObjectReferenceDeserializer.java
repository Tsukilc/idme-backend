package com.tsukilc.idme.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * ObjectReference 反序列化器
 * 支持从String（仅ID）或Object（包含id和其他字段）反序列化
 */
public class ObjectReferenceDeserializer extends JsonDeserializer<ObjectReference> {
    
    @Override
    public ObjectReference deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        if (node == null || node.isNull()) {
            return null;
        }
        
        // 如果是字符串，直接作为ID
        if (node.isTextual()) {
            return new ObjectReference(node.asText());
        }
        
        // 如果是对象，提取id和displayName
        if (node.isObject()) {
            ObjectReference ref = new ObjectReference();
            if (node.has("id")) {
                ref.setId(node.get("id").asText());
            }
            if (node.has("displayName")) {
                ref.setDisplayName(node.get("displayName").asText());
            }
            // 兼容"name"字段（某些SDK返回可能用name而不是displayName）
            if (node.has("name") && ref.getDisplayName() == null) {
                ref.setDisplayName(node.get("name").asText());
            }
            return ref;
        }
        
        return null;
    }
}
