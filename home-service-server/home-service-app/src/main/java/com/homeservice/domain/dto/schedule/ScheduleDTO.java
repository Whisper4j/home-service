package com.homeservice.domain.dto.schedule;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;

import lombok.Data;

import org.hibernate.validator.constraints.UniqueElements;

@Data
public class ScheduleDTO {

    @NotNull(message = MessageConstant.WORK_INTERVAL_REQUIRED)
    @Size(min = 1, max = 14, message = MessageConstant.WORK_INTERVAL_COUNT_INVALID)
    @Valid
    private List<@NotNull(message = MessageConstant.WORK_INTERVAL_ITEM_INVALID) @Valid WorkIntervalDTO> intervals; // 工作时段列表

    @NotNull(message = MessageConstant.REST_WEEKDAYS_REQUIRED)
    @Size(max = 7, message = MessageConstant.REST_WEEKDAYS_TOO_MANY)
    @UniqueElements(message = MessageConstant.REST_WEEKDAYS_DUPLICATED)
    @Valid
    private List<@NotNull(message = MessageConstant.REST_WEEKDAY_INVALID) @Min(value = 1, message = MessageConstant.REST_WEEKDAY_INVALID) @Max(value = 7, message = MessageConstant.REST_WEEKDAY_INVALID) Integer> restWeekdays; // 休息星期列表

    @AssertTrue(message = MessageConstant.WORK_INTERVAL_OVERLAPPED)
    @JsonIgnore
    public boolean isNonOverlapping() {
        if (intervals == null
                || intervals.stream()
                        .anyMatch(x -> x == null || x.getStart() == null || x.getEnd() == null))
            return true;
        var sorted =
                intervals.stream()
                        .sorted(java.util.Comparator.comparing(WorkIntervalDTO::getStart))
                        .toList();
        for (int i = 1; i < sorted.size(); i++)
            if (sorted.get(i).getStart().isBefore(sorted.get(i - 1).getEnd())) return false;
        return true;
    }
}
