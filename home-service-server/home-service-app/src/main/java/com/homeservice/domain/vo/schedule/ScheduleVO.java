package com.homeservice.domain.vo.schedule;

import com.homeservice.domain.dto.schedule.WorkIntervalDTO;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleVO {

    private Boolean configured; // 是否已配置

    private List<WorkIntervalDTO> intervals; // 工作时段列表

    private List<Integer> restWeekdays; // 休息星期列表
}
