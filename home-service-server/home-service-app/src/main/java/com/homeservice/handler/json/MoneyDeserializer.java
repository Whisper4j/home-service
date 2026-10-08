package com.homeservice.handler.json;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.math.BigDecimal;
public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {
    public BigDecimal deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING)) return (BigDecimal) context.handleUnexpectedToken(BigDecimal.class, p);
        String value = p.getText().strip();
        if (!value.matches("(0|[1-9][0-9]{0,8})\\.[0-9]{2}")) throw JsonMappingException.from(p, "金额必须是两位小数字符串");
        return new BigDecimal(value);
    }
}
