package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 支付枚举类
 * 定义支付的固定取值
 */
@RequiredArgsConstructor
public enum PaymentType {
    PAYMENT("PAYMENT"),
    TOP_UP("TOP_UP"),
    PARTIAL_REFUND("PARTIAL_REFUND"),
    FULL_REFUND("FULL_REFUND");
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
    public static PaymentType fromValue(String value) {
        for (PaymentType item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的PaymentType");
    }
}
