package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 订单状态枚举类
 * 定义订单状态的固定取值
 */
@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_PAYMENT("PENDING_PAYMENT", "待支付"),
    WAITING_DISPATCH("WAITING_DISPATCH", "等待派单"),
    WAITING_ACCEPTANCE("WAITING_ACCEPTANCE", "等待接单"),
    PENDING_SERVICE("PENDING_SERVICE", "待服务"),
    DEPARTED("DEPARTED", "已出发"),
    ARRIVED("ARRIVED", "已到达"),
    IN_SERVICE("IN_SERVICE", "服务中"),
    PENDING_CONFIRMATION("PENDING_CONFIRMATION", "待确认"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");

    @EnumValue
    private final String value;
    private final String description;

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
