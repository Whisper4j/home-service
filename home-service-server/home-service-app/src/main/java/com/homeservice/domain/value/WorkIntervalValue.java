package com.homeservice.domain.value;
import java.time.LocalTime;
/** SQL JSON 的明确对象元素，与接口 DTO 解耦。 */
public record WorkIntervalValue(LocalTime start, LocalTime end) {}
