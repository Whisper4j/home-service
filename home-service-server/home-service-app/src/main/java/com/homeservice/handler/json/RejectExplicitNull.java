package com.homeservice.handler.json;

import java.lang.annotation.*;

/**
 * 拒绝显式空值注解类
 * 标记并约束拒绝显式空值字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.PARAMETER})
public @interface RejectExplicitNull {}
