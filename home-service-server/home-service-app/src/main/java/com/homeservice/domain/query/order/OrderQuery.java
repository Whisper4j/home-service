package com.homeservice.domain.query.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homeservice.enums.BookingType;
import com.homeservice.enums.OrderStatus;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.UniqueElements;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = "搜索关键词长度必须在1到100之间")
    private String keyword; // 搜索关键词
    private OrderStatus status; // 状态
    private BookingType bookingType; // 预约类型
    private LocalDate from; // 开始日期
    private LocalDate to; // 结束日期
    @Size(min = 1, max = 10, message = "状态列表数量必须在1到10之间")
    @UniqueElements(message = "状态列表不能重复")
    @Valid
    private List<@NotNull(message = "状态列表元素不能为空") OrderStatus> statuses; // 状态列表

    @AssertTrue(message = "status 与 statuses 互斥")
    @JsonIgnore
    public boolean isExclusiveStatus() {
        return status == null || statuses == null;
    }

    @AssertTrue(message = "from 不得晚于 to")
    @JsonIgnore
    public boolean isValidRange() {
        return from == null || to == null || !from.isAfter(to);
    }
}
