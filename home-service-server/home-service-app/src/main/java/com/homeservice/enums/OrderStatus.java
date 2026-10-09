package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 订单状态枚举类
 * 定义订单状态的固定取值
 */
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_PAYMENT("PENDING_PAYMENT"),
    WAITING_DISPATCH("WAITING_DISPATCH"),
    WAITING_ACCEPTANCE("WAITING_ACCEPTANCE"),
    PENDING_SERVICE("PENDING_SERVICE"),
    DEPARTED("DEPARTED"),
    ARRIVED("ARRIVED"),
    IN_SERVICE("IN_SERVICE"),
    PENDING_CONFIRMATION("PENDING_CONFIRMATION"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");
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
    public static OrderStatus fromValue(String value) {
        for (OrderStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的OrderStatus");
    }
}
