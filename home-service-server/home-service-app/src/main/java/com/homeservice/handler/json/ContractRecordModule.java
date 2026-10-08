package com.homeservice.handler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.*;

/** Jackson 的 Nulls.FAIL 会同时拒绝 record 缺参；这里显式区分契约的缺失与 null。 */
public class ContractRecordModule extends SimpleModule {
    public ContractRecordModule() {
        setDeserializerModifier(new BeanDeserializerModifier() {
            @Override public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription bean, JsonDeserializer<?> delegate) {
                if (!bean.getBeanClass().isRecord()) return delegate;
                Set<String> fields = new HashSet<>();
                for (var component : bean.getBeanClass().getRecordComponents())
                    if (component.isAnnotationPresent(RejectExplicitNull.class)) fields.add(component.getName());
                return fields.isEmpty() ? delegate : new ShapeDeserializer(delegate, fields);
            }
        });
    }
    private static class ShapeDeserializer extends DelegatingDeserializer {
        private final Set<String> fields;
        ShapeDeserializer(JsonDeserializer<?> delegate, Set<String> fields) { super(delegate); this.fields = fields; }
        @Override protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) { return new ShapeDeserializer(delegate, fields); }
        @Override public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode tree = context.readTree(parser);
            for (String field : fields) {
                JsonNode value = tree.get(field);
                if (value != null && value.isNull()) throw JsonMappingException.from(parser, "字段不接受 null: " + field);
                if (value != null && value.isArray()) for (JsonNode element : value)
                    if (element.isNull()) throw JsonMappingException.from(parser, "数组不接受 null 元素: " + field);
            }
            try (JsonParser replay = tree.traverse(parser.getCodec())) {
                replay.nextToken();
                return _delegatee.deserialize(replay, context);
            }
        }
    }
}
