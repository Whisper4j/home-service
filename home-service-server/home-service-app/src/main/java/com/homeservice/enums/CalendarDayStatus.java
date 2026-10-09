package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 日历日期状态枚举类
 * 定义日历日期状态的固定取值
 */
@RequiredArgsConstructor
public enum CalendarDayStatus {
    UNCONFIGURED("UNCONFIGURED"),
    REST("REST"),
    AVAILABLE("AVAILABLE"),
    ARRANGED("ARRANGED"),
    LEAVE("LEAVE");
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
    public static CalendarDayStatus fromValue(String value) {
        for (CalendarDayStatus item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的CalendarDayStatus");
    }
}
