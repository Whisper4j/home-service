package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 目录状态枚举类
 * 定义目录状态的固定取值
 */
@RequiredArgsConstructor
public enum CatalogStatus {
    ON_SHELF("ON_SHELF"),
    OFF_SHELF("OFF_SHELF");
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
    public static CatalogStatus fromValue(String value) {
        for (CatalogStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的CatalogStatus");
    }
}
