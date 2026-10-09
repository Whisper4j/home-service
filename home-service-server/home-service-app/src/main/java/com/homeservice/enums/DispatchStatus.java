package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 派单状态枚举类
 * 定义派单状态的固定取值
 */
@RequiredArgsConstructor
public enum DispatchStatus {
    NOT_REQUIRED("NOT_REQUIRED"),
    PENDING("PENDING"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED");
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
    public static DispatchStatus fromValue(String value) {
        for (DispatchStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的DispatchStatus");
    }
}
