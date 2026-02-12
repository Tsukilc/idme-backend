package com.tsukilc.idme.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * PartnerTypeRef 序列化器
 * 输出enName字符串（用于create/update SDK调用）
 */
public class PartnerTypeRefSerializer extends JsonSerializer<PartnerTypeRef> {
    
    @Override
    public void serialize(PartnerTypeRef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.getEnName() == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.getEnName());
        }
    }
}
