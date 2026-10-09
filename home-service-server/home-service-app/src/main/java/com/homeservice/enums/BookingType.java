package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 预约枚举类
 * 定义预约的固定取值
 */
@Getter
@RequiredArgsConstructor
public enum BookingType {
    STANDARD("STANDARD", "标准预约"),
    OFFER("OFFER", "优惠预约");

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
    public static BookingType fromValue(String value) {
        for (BookingType item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的BookingType");
    }
}
