package com.homeservice.validation;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.*;

import java.lang.annotation.*;

/**
 * 倍数约束注解类
 * 标记并约束倍数约束字段
 */
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT,
    ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipleOfValidator.class)
public @interface MultipleOf {
    /**
     * 定义校验失败时的提示信息
     */
    String message() default MessageConstant.PARAMETER_INVALID;

    /**
     * 定义校验分组
     */
    Class<?>[] groups() default {};

    /**
     * 定义校验负载信息
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 定义注解使用的约束值
     */
    int value();
}
