package com.tsukilc.idme.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * WorkingStateRef序列化器
 * 序列化时输出String枚举值（SDK创建/更新接口要求）
 */
public class WorkingStateRefSerializer extends JsonSerializer<WorkingStateRef> {
    @Override
    public void serialize(WorkingStateRef value, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        if (value == null || value.getCode() == null) {
            gen.writeNull();
        } else {
            // 序列化为String枚举值
            gen.writeString(value.getCode());
        }
    }
}
