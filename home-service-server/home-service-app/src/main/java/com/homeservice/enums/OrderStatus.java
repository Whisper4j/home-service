package com.homeservice.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
/** 稳定代码：数据库与 JSON 都保存 value，禁止使用 ordinal。 */
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
    @EnumValue private final String value;
    @JsonValue public String getValue() { return value; }
    @JsonCreator public static OrderStatus fromValue(String value) {
        for (OrderStatus item : values()) if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的OrderStatus");
    }
}
