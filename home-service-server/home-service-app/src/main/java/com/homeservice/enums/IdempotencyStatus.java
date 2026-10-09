package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 幂等状态枚举类
 * 定义幂等状态的固定取值
 */
@RequiredArgsConstructor
public enum IdempotencyStatus {
    PROCESSING("PROCESSING"),
    SUCCEEDED("SUCCEEDED");
    @EnumValue
    private final String value;

    /**
     * 获取枚举对应的数据库和JSON值
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * 根据字符串解析枚举值
     */
    @JsonCreator
    public static IdempotencyStatus fromValue(String value) {
        for (IdempotencyStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的IdempotencyStatus");
    }
}
