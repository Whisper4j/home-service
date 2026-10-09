package com.homeservice.enums;

import com.homeservice.common.constant.MessageConstant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 目录状态枚举类
 * 定义目录状态的固定取值
 */
@Getter
@RequiredArgsConstructor
public enum CatalogStatus {
    ON_SHELF("ON_SHELF", "上架"),
    OFF_SHELF("OFF_SHELF", "下架");

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
    public static CatalogStatus fromValue(String value) {
        for (CatalogStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException(MessageConstant.ENUM_VALUE_INVALID);
    }
}
