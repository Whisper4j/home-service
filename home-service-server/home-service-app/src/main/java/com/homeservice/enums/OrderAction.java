package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 订单操作枚举类
 * 定义订单操作的固定取值
 */
@RequiredArgsConstructor
public enum OrderAction {
    PAY("PAY"),
    CANCEL("CANCEL"),
    CHANGE_OFFER("CHANGE_OFFER"),
    VIEW_START_CODE("VIEW_START_CODE"),
    DEPART("DEPART"),
    ARRIVE("ARRIVE"),
    START("START"),
    FINISH("FINISH"),
    CONFIRM("CONFIRM"),
    REVIEW("REVIEW");
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
    public static OrderAction fromValue(String value) {
        for (OrderAction item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的OrderAction");
    }
}
