package com.homeservice.handler.json;

import java.lang.annotation.*;

/** 可省略的非 nullable 字段：缺失合法，显式 null 非法。 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.PARAMETER})
public @interface RejectExplicitNull {}
