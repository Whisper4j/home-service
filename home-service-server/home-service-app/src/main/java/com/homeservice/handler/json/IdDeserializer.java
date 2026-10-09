package com.homeservice.handler.json;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 * 编号反序列化器类
 * 将JSON输入解析为编号
 */
public class IdDeserializer extends JsonDeserializer<Long> {
    /**
     * 解析并校验输入数据
     */
    public static long parse(String value) {
        if (value == null || !value.strip().matches("[1-9][0-9]{0,18}"))
            throw new IllegalArgumentException(MessageConstant.ID_FORMAT_INVALID);
        return Long.parseLong(value.strip());
    }

    /**
     * 反序列化并校验输入数据
     */
    public Long deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING))
            return (Long) context.handleUnexpectedToken(Long.class, p);
        try {
            return parse(p.getText());
        } catch (IllegalArgumentException e) {
            throw JsonMappingException.from(p, MessageConstant.ID_FORMAT_INVALID);
        }
    }
}
