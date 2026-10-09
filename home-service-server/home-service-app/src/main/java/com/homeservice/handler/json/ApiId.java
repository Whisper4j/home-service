package com.homeservice.handler.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.*;

import java.lang.annotation.*;

/**
 * 接口编号注解类
 * 标记并约束接口编号字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT
})
@JacksonAnnotationsInside
@JsonSerialize(using = IdSerializer.class)
@JsonDeserialize(using = IdDeserializer.class)
public @interface ApiId {}
