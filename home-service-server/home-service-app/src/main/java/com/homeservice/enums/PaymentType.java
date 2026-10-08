package com.homeservice.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
/** 稳定代码：数据库与 JSON 都保存 value，禁止使用 ordinal。 */
@RequiredArgsConstructor
public enum PaymentType {
    PAYMENT("PAYMENT"),
    TOP_UP("TOP_UP"),
    PARTIAL_REFUND("PARTIAL_REFUND"),
    FULL_REFUND("FULL_REFUND");
    @EnumValue private final String value;
    @JsonValue public String getValue() { return value; }
    @JsonCreator public static PaymentType fromValue(String value) {
        for (PaymentType item : values()) if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的PaymentType");
    }
}
