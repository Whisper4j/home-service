package com.homeservice.handler.json;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.time.*;
public class HalfHourTimeDeserializer extends JsonDeserializer<LocalTime> {
    public static LocalTime parse(String input) {
        String value = input.strip();
        if (!value.matches("([01][0-9]|2[0-3]):(00|30)")) throw new IllegalArgumentException("时间需 HH:mm 半小时对齐");
        return LocalTime.parse(value);
    }
    public LocalTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING)) return (LocalTime) context.handleUnexpectedToken(LocalTime.class, p);
        try { return parse(p.getText()); }
        catch (IllegalArgumentException | DateTimeException e) { throw JsonMappingException.from(p, "时间格式无效"); }
    }
}
