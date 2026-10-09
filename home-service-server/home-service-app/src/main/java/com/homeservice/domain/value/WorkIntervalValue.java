package com.homeservice.domain.value;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkIntervalValue {

    private LocalTime start; // 开始时间
    private LocalTime end; // 结束时间
}
