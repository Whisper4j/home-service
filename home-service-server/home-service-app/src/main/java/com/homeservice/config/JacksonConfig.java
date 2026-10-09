package com.homeservice.config;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.homeservice.handler.json.*;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Jackson配置类
 * 配置接口JSON的序列化和反序列化规则
 */
@Configuration(proxyBeanMethods = false)
public class JacksonConfig {
    /**
     * 创建契约记录对象的Jackson解析模块
     */
    @Bean
    public com.fasterxml.jackson.databind.Module contractRecordModule() {
        return new ContractRecordModule();
    }

    /**
     * 配置严格的JSON转换规则
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer strictJson() {
        return builder -> {
            builder.featuresToEnable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS,
                    DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
            builder.featuresToDisable(
                    DeserializationFeature.ACCEPT_FLOAT_AS_INT,
                    DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                    MapperFeature.ALLOW_COERCION_OF_SCALARS);
            builder.deserializerByType(String.class, new TrimmedStringDeserializer());
            builder.deserializerByType(OffsetDateTime.class, new ShanghaiDateTimeDeserializer());
            builder.serializerByType(OffsetDateTime.class, new ShanghaiDateTimeSerializer());
            builder.deserializerByType(LocalDate.class, new StrictLocalDateDeserializer());
            builder.deserializerByType(LocalTime.class, new HalfHourTimeDeserializer());
            builder.serializerByType(
                    LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm")));
            builder.postConfigurer(
                    mapper -> {
                        mapper.getFactory()
                                .enable(
                                        StreamReadFeature.STRICT_DUPLICATE_DETECTION
                                                .mappedFeature());
                        for (LogicalType type :
                                new LogicalType[] {
                                    LogicalType.Integer, LogicalType.Boolean, LogicalType.Enum
                                }) {
                            mapper.coercionConfigFor(type)
                                    .setCoercion(
                                            CoercionInputShape.EmptyString, CoercionAction.Fail);
                        }
                        mapper.coercionConfigFor(LogicalType.Textual)
                                .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                                .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
                    });
        };
    }
}
