package com.homeservice.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
/** 稳定代码：数据库与 JSON 都保存 value，禁止使用 ordinal。 */
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
    @EnumValue private final String value;
    @JsonValue public String getValue() { return value; }
    @JsonCreator public static OrderAction fromValue(String value) {
        for (OrderAction item : values()) if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的OrderAction");
    }
}
