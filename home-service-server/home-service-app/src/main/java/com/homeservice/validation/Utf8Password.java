package com.homeservice.validation;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.*;

import java.lang.annotation.*;

/**
 * UTF8密码注解类
 * 标记并约束UTF8密码字段
 */
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT,
    ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Utf8PasswordValidator.class)
public @interface Utf8Password {
    /**
     * 定义校验失败时的提示信息
     */
    String message() default MessageConstant.PASSWORD_BYTES_TOO_LONG;

    /**
     * 定义校验分组
     */
    Class<?>[] groups() default {};

    /**
     * 定义校验负载信息
     */
    Class<? extends Payload>[] payload() default {};
}
