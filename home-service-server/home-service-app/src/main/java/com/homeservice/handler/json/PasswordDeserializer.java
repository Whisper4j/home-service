package com.homeservice.handler.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 * 密码反序列化器类
 * 将JSON输入解析为密码
 */
public class PasswordDeserializer extends JsonDeserializer<String> {
    /**
     * 反序列化并校验输入数据
     */
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING))
            return (String) context.handleUnexpectedToken(String.class, p);
        return p.getText();
    }
}
