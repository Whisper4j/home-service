package com.homeservice.domain.dto.schedule;

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

    @NotNull(message = "工作时段列表不能为空")
    @Size(min = 1, max = 14, message = "工作时段列表数量必须在1到14之间")
    @Valid
    private List<@NotNull(message = "工作时段列表元素不能为空") @Valid WorkIntervalDTO> intervals; // 工作时段列表

    @NotNull(message = "休息星期列表（JSON）不能为空")
    @Size(max = 7, message = "休息星期列表数量不能超过7")
    @UniqueElements(message = "休息星期列表（JSON）不能重复")
    @Valid
    private List<@NotNull(message = "休息星期元素不能为空") @Min(value = 1, message = "休息星期不能小于1") @Max(value = 7, message = "休息星期不能大于7") Integer> restWeekdays; // 休息星期列表

    @AssertTrue(message = "工作区间不可重叠")
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
