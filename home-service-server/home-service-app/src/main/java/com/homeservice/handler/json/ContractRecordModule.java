package com.homeservice.handler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.homeservice.common.constant.MessageConstant;

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
                        Set<String> rejectNullFields = new HashSet<>();
                        Set<String> requiredFields = new HashSet<>();
                        for (Class<?> type = bean.getBeanClass();
                                type != null && type != Object.class;
                                type = type.getSuperclass()) {
                            for (var field : type.getDeclaredFields()) {
                                if (field.isAnnotationPresent(RejectExplicitNull.class))
                                    rejectNullFields.add(field.getName());
                                JsonProperty property = field.getAnnotation(JsonProperty.class);
                                if (property != null && property.required())
                                    requiredFields.add(field.getName());
                            }
                        }
                        return rejectNullFields.isEmpty() && requiredFields.isEmpty()
                                ? delegate
                                : new ShapeDeserializer(delegate, rejectNullFields, requiredFields);
                    }
                });
    }

    /**
     * 契约对象反序列化器类
     * 将JSON输入解析为结构
     */
    private static class ShapeDeserializer extends DelegatingDeserializer {

        private final Set<String> rejectNullFields;
        private final Set<String> requiredFields;

        /**
         * 创建结构Deserializer实例
         */
        ShapeDeserializer(
                JsonDeserializer<?> delegate,
                Set<String> rejectNullFields,
                Set<String> requiredFields) {
            super(delegate);
            this.rejectNullFields = rejectNullFields;
            this.requiredFields = requiredFields;
        }

        /**
         * 创建记录对象委托反序列化器
         */
        @Override
        protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
            return new ShapeDeserializer(delegate, rejectNullFields, requiredFields);
        }

        /**
         * 反序列化并校验输入数据
         */
        @Override
        public Object deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {
            JsonNode tree = context.readTree(parser);
            for (String field : requiredFields)
                if (!tree.has(field))
                    throw JsonMappingException.from(parser, MessageConstant.REQUEST_INCOMPLETE);
            for (String field : rejectNullFields) {
                JsonNode value = tree.get(field);
                if (value != null && value.isNull())
                    throw JsonMappingException.from(parser, MessageConstant.REQUEST_INVALID);
                if (value != null && value.isArray())
                    for (JsonNode element : value)
                        if (element.isNull())
                            throw JsonMappingException.from(parser, MessageConstant.REQUEST_INVALID);
            }
            try (JsonParser replay = tree.traverse(parser.getCodec())) {
                replay.nextToken();
                return _delegatee.deserialize(replay, context);
            }
        }
    }
}
