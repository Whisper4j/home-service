package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * WebSocket事件枚举类
 * 定义WebSocket事件的固定取值
 */
@RequiredArgsConstructor
public enum WsEventType {
    OFFER_CREATED("OFFER_CREATED"),
    OFFER_PRICE_CHANGED("OFFER_PRICE_CHANGED"),
    ORDER_CLAIMED("ORDER_CLAIMED"),
    ORDER_CLOSED("ORDER_CLOSED"),
    DISPATCH_SUCCEEDED("DISPATCH_SUCCEEDED"),
    DISPATCH_FAILED("DISPATCH_FAILED"),
    ORDER_STATUS_CHANGED("ORDER_STATUS_CHANGED");
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
    public static WsEventType fromValue(String value) {
        for (WsEventType item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的WsEventType");
    }
}
