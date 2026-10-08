package com.homeservice.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
/** 稳定代码：数据库与 JSON 都保存 value，禁止使用 ordinal。 */
@RequiredArgsConstructor
public enum WsEventType {
    OFFER_CREATED("OFFER_CREATED"),
    OFFER_PRICE_CHANGED("OFFER_PRICE_CHANGED"),
    ORDER_CLAIMED("ORDER_CLAIMED"),
    ORDER_CLOSED("ORDER_CLOSED"),
    DISPATCH_SUCCEEDED("DISPATCH_SUCCEEDED"),
    DISPATCH_FAILED("DISPATCH_FAILED"),
    ORDER_STATUS_CHANGED("ORDER_STATUS_CHANGED");
    @EnumValue private final String value;
    @JsonValue public String getValue() { return value; }
    @JsonCreator public static WsEventType fromValue(String value) {
        for (WsEventType item : values()) if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的WsEventType");
    }
}
