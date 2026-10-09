package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 时间槽状态枚举类
 * 定义时间槽状态的固定取值
 */
@RequiredArgsConstructor
public enum SlotStatus {
    NON_WORKING("NON_WORKING"),
    AVAILABLE("AVAILABLE"),
    LEAVE("LEAVE"),
    SERVICE("SERVICE"),
    BUFFER("BUFFER");
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
    public static SlotStatus fromValue(String value) {
        for (SlotStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的SlotStatus");
    }
}
