package com.tsukilc.idme.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * ObjectReference 序列化器
 * 在发送给SDK时，序列化为包含id和displayName的对象
 * 如果displayName为null，只序列化id
 */
public class ObjectReferenceSerializer extends JsonSerializer<ObjectReference> {
    
    @Override
    public void serialize(ObjectReference value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        
        // 序列化为对象 {"id": "xxx", "displayName": "xxx"}
        gen.writeStartObject();
        if (value.getId() != null) {
            gen.writeStringField("id", value.getId());
        }
        if (value.getDisplayName() != null) {
            gen.writeStringField("displayName", value.getDisplayName());
        }
        gen.writeEndObject();
    }
}
