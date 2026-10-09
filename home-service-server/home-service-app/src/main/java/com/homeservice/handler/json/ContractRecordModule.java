package com.homeservice.handler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.*;

/**
 * 契约记录解析模块类
 * 为契约记录对象注册严格空值检查
 */
public class ContractRecordModule extends SimpleModule {
    /**
     * 创建契约记录模块实例
     */
    public ContractRecordModule() {
        setDeserializerModifier(
                new BeanDeserializerModifier() {
                    /**
                     * 为契约记录对象包装反序列化器
                     */
                    @Override
                    public JsonDeserializer<?> modifyDeserializer(
                            DeserializationConfig config,
                            BeanDescription bean,
                            JsonDeserializer<?> delegate) {
                        if (!bean.getBeanClass().isRecord()) return delegate;
                        Set<String> fields = new HashSet<>();
                        for (var component : bean.getBeanClass().getRecordComponents())
                            if (component.isAnnotationPresent(RejectExplicitNull.class))
                                fields.add(component.getName());
                        return fields.isEmpty()
                                ? delegate
                                : new ShapeDeserializer(delegate, fields);
                    }
                });
    }

    /**
     * 契约对象反序列化器类
     * 将JSON输入解析为结构
     */
    private static class ShapeDeserializer extends DelegatingDeserializer {

        private final Set<String> fields;

        /**
         * 创建结构Deserializer实例
         */
        ShapeDeserializer(JsonDeserializer<?> delegate, Set<String> fields) {
            super(delegate);
            this.fields = fields;
        }

        /**
         * 创建记录对象委托反序列化器
         */
        @Override
        protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
            return new ShapeDeserializer(delegate, fields);
        }

        /**
         * 反序列化并校验输入数据
         */
        @Override
        public Object deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {
            JsonNode tree = context.readTree(parser);
            for (String field : fields) {
                JsonNode value = tree.get(field);
                if (value != null && value.isNull())
                    throw JsonMappingException.from(parser, "字段不接受 null: " + field);
                if (value != null && value.isArray())
                    for (JsonNode element : value)
                        if (element.isNull())
                            throw JsonMappingException.from(parser, "数组不接受 null 元素: " + field);
            }
            try (JsonParser replay = tree.traverse(parser.getCodec())) {
                replay.nextToken();
                return _delegatee.deserialize(replay, context);
            }
        }
    }
}
