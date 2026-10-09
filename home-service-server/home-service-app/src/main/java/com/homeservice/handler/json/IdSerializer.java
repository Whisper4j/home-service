package com.homeservice.handler.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 * 编号序列化器类
 * 将编号转换为JSON输出格式
 */
public class IdSerializer extends JsonSerializer<Long> {
    /**
     * 序列化接口输出数据
     */
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value <= 0) throw JsonMappingException.from(gen, "ID 必须为正数");
        gen.writeString(value.toString());
    }
}
