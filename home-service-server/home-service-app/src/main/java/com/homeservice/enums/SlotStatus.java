package com.homeservice.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
/** 稳定代码：数据库与 JSON 都保存 value，禁止使用 ordinal。 */
@RequiredArgsConstructor
public enum SlotStatus {
    NON_WORKING("NON_WORKING"),
    AVAILABLE("AVAILABLE"),
    LEAVE("LEAVE"),
    SERVICE("SERVICE"),
    BUFFER("BUFFER");
    @EnumValue private final String value;
    @JsonValue public String getValue() { return value; }
    @JsonCreator public static SlotStatus fromValue(String value) {
        for (SlotStatus item : values()) if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的SlotStatus");
    }
}
