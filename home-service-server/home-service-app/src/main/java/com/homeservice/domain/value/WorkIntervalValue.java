package com.homeservice.domain.value;

import java.time.LocalTime;

/**
 * 工作时段值对象类
 * 表达工作时段相关业务值
 */
public record WorkIntervalValue(LocalTime start, LocalTime end) {}
