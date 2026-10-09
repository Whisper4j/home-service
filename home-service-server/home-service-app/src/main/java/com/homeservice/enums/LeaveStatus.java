package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 请假状态枚举类
 * 定义请假状态的固定取值
 */
@RequiredArgsConstructor
public enum LeaveStatus {
    ACTIVE("ACTIVE"),
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
    public static LeaveStatus fromValue(String value) {
        for (LeaveStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的LeaveStatus");
    }
}
