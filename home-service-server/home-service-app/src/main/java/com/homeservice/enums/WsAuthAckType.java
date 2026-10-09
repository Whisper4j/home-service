package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * WebSocket认证确认枚举类
 * 定义WebSocket认证确认的固定取值
 */
@RequiredArgsConstructor
public enum WsAuthAckType {
    AUTHENTICATED("AUTHENTICATED");
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
    public static WsAuthAckType fromValue(String value) {
        for (WsAuthAckType item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的WsAuthAckType");
    }
}
