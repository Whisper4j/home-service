package com.homeservice.handler.json;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.time.*;

/**
 * 上海日期时间反序列化器类
 * 将JSON输入解析为上海日期时间
 */
public class ShanghaiDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    /**
     * 解析并校验输入数据
     */
    public static OffsetDateTime parse(String input) {
        String value = input.strip();
        if (!value.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\+08:00"))
            throw new IllegalArgumentException(MessageConstant.DATE_TIME_FORMAT_INVALID);
        return OffsetDateTime.parse(value);
    }

    /**
     * 反序列化并校验输入数据
     */
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING))
            return (OffsetDateTime) context.handleUnexpectedToken(OffsetDateTime.class, p);
        try {
            return parse(p.getText());
        } catch (IllegalArgumentException | DateTimeException e) {
            throw JsonMappingException.from(p, MessageConstant.DATE_TIME_FORMAT_INVALID);
        }
    }
}
