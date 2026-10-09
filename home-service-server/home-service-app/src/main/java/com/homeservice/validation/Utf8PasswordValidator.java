package com.homeservice.validation;

import jakarta.validation.*;

/**
 * UTF8密码校验器类
 * 校验UTF8密码输入值
 */
public class Utf8PasswordValidator implements ConstraintValidator<Utf8Password, String> {

    /**
     * 校验输入值是否符合约束
     */
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null
                || value.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72;
    }
}
