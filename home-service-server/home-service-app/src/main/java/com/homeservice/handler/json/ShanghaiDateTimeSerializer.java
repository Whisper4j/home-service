package com.homeservice.handler.json;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
public class ShanghaiDateTimeSerializer extends JsonSerializer<OffsetDateTime> {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssXXX");
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.withOffsetSameInstant(ZoneOffset.ofHours(8)).withNano(0).format(FORMAT));
    }
}
