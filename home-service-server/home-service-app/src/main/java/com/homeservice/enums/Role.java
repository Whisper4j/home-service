package com.homeservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 角色枚举类
 * 定义角色的固定取值
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    CUSTOMER("CUSTOMER", "客户"),
    WORKER("WORKER", "服务人员"),
    ADMIN("ADMIN", "管理员");

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
    public static Role fromValue(String value) {
        for (Role item : values())
            if (item.value.equals(value == null ? null : value.strip())) return item;
        throw new IllegalArgumentException("无效的Role");
    }
}
