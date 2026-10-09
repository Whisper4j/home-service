package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 派单尝试结果枚举类
 * 定义派单尝试结果的固定取值
 */
@RequiredArgsConstructor
public enum DispatchAttemptResult {
    ASSIGNED("ASSIGNED"),
    INELIGIBLE("INELIGIBLE"),
    SLOT_CONFLICT("SLOT_CONFLICT"),
    NO_CANDIDATE("NO_CANDIDATE");
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
    public static DispatchAttemptResult fromValue(String value) {
        for (DispatchAttemptResult item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的DispatchAttemptResult");
    }
}
