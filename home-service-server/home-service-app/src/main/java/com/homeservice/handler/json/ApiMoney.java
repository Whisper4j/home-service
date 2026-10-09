package com.homeservice.handler.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.*;

import java.lang.annotation.*;

/**
 * 接口金额注解类
 * 标记并约束接口金额字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT
})
@JacksonAnnotationsInside
@JsonSerialize(using = MoneySerializer.class)
@JsonDeserialize(using = MoneyDeserializer.class)
public @interface ApiMoney {}
