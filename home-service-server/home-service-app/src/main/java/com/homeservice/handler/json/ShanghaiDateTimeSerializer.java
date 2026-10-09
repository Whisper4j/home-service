package com.homeservice.handler.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 上海日期时间序列化器类
 * 将上海日期时间转换为JSON输出格式
 */
public class ShanghaiDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssXXX");

    /**
     * 序列化接口输出数据
     */
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(
                value.withOffsetSameInstant(ZoneOffset.ofHours(8)).withNano(0).format(FORMAT));
    }
}
