package com.homeservice.handler.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.*;

import java.lang.annotation.*;

/**
 * 接口编号集合注解类
 * 标记并约束接口编号集合字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT
})
@JacksonAnnotationsInside
@JsonSerialize(contentUsing = IdSerializer.class)
@JsonDeserialize(contentUsing = IdDeserializer.class)
public @interface ApiIds {}
