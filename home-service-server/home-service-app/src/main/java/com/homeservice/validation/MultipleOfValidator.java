package com.homeservice.validation;

import jakarta.validation.*;

/**
 * 倍数约束校验器类
 * 校验倍数约束输入值
 */
public class MultipleOfValidator implements ConstraintValidator<MultipleOf, Integer> {

    private int divisor;

    /**
     * 初始化校验器参数
     */
    public void initialize(MultipleOf annotation) {
        divisor = annotation.value();
    }

    /**
     * 校验输入值是否符合约束
     */
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || value % divisor == 0;
    }
}
