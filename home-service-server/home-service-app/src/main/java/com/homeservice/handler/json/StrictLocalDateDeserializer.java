package com.homeservice.handler.json;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.time.*;
public class StrictLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    public static LocalDate parse(String input) {
        String value = input.strip();
        if (!value.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) throw new IllegalArgumentException("日期格式无效");
        return LocalDate.parse(value);
    }
    public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING)) return (LocalDate) context.handleUnexpectedToken(LocalDate.class, p);
        try { return parse(p.getText()); }
        catch (IllegalArgumentException | DateTimeException e) { throw JsonMappingException.from(p, "日期格式无效"); }
    }
}
