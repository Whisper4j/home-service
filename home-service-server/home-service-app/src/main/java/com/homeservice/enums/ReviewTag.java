package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 评价标签枚举类
 * 定义评价标签的固定取值
 */
@RequiredArgsConstructor
public enum ReviewTag {
    PUNCTUAL("PUNCTUAL"),
    PROFESSIONAL("PROFESSIONAL"),
    FRIENDLY("FRIENDLY");
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
    public static ReviewTag fromValue(String value) {
        for (ReviewTag item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的ReviewTag");
    }
}
