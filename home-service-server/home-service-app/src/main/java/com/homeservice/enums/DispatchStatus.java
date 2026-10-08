package com.homeservice.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
/** 稳定代码：数据库与 JSON 都保存 value，禁止使用 ordinal。 */
@RequiredArgsConstructor
public enum DispatchStatus {
    NOT_REQUIRED("NOT_REQUIRED"),
    PENDING("PENDING"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED");
    @EnumValue private final String value;
    @JsonValue public String getValue() { return value; }
    @JsonCreator public static DispatchStatus fromValue(String value) {
        for (DispatchStatus item : values()) if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的DispatchStatus");
    }
}
