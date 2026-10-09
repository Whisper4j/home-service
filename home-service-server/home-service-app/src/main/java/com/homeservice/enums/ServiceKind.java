package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

/**
 * 服务类型枚举类
 * 定义服务类型的固定取值
 */
@RequiredArgsConstructor
public enum ServiceKind {
    CLEANING("CLEANING"),
    REPAIR("REPAIR"),
    OTHER("OTHER");
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
    public static ServiceKind fromValue(String value) {
        for (ServiceKind item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的ServiceKind");
    }
}
