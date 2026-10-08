package com.homeservice.handler.json;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
/** 只接收 JSON 字符串，拒绝数字或布尔值的隐式转换。 */
public class TrimmedStringDeserializer extends JsonDeserializer<String> {
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING)) return (String) context.handleUnexpectedToken(String.class, p);
        return p.getText().strip();
    }
}
