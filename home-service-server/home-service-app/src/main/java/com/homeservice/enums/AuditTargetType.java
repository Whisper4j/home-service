package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 审计目标枚举类
 * 定义审计目标的固定取值
 */
@RequiredArgsConstructor
public enum AuditTargetType {
    ACCOUNT("ACCOUNT"),
    WORKER("WORKER"),
    CATEGORY("CATEGORY"),
    SERVICE_ITEM("SERVICE_ITEM"),
    SKU("SKU"),
    SKILL("SKILL"),
    ORDER("ORDER"),
    SETTINGS("SETTINGS"),
    SCENE_IMAGE("SCENE_IMAGE");
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
    public static AuditTargetType fromValue(String value) {
        for (AuditTargetType item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的AuditTargetType");
    }
}
