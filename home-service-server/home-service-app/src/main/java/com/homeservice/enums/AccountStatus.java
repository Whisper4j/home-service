package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 账号状态枚举类
 * 定义账号状态的固定取值
 */
@Getter
@RequiredArgsConstructor
public enum AccountStatus {
    ENABLED("ENABLED", "启用"),
    DISABLED("DISABLED", "禁用");

    @EnumValue
    private final String value;
    private final String description;

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
    public static AccountStatus fromValue(String value) {
        for (AccountStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的AccountStatus");
    }
}
