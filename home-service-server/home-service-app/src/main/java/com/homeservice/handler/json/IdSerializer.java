package com.homeservice.handler.json;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
public class IdSerializer extends JsonSerializer<Long> {
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value <= 0) throw JsonMappingException.from(gen, "ID 必须为正数");
        gen.writeString(value.toString());
    }
}
