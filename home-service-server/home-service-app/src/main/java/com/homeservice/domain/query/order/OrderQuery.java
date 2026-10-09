package com.homeservice.domain.query.order;

import com.homeservice.common.constant.MessageConstant;

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
    @Size(min = 1, max = 100, message = MessageConstant.SEARCH_KEYWORD_TOO_LONG)
    private String keyword; // 搜索关键词
    private OrderStatus status; // 状态
    private BookingType bookingType; // 预约类型
    private LocalDate from; // 开始日期
    private LocalDate to; // 结束日期
    @Size(min = 1, max = 10, message = MessageConstant.STATUS_FILTER_INVALID)
    @UniqueElements(message = MessageConstant.STATUS_FILTER_INVALID)
    @Valid
    private List<@NotNull(message = MessageConstant.STATUS_FILTER_INVALID) OrderStatus> statuses; // 状态列表

    @AssertTrue(message = MessageConstant.STATUS_FILTER_INVALID)
    @JsonIgnore
    public boolean isExclusiveStatus() {
        return status == null || statuses == null;
    }

    @AssertTrue(message = MessageConstant.DATE_RANGE_INVALID)
    @JsonIgnore
    public boolean isValidRange() {
        return from == null || to == null || !from.isAfter(to);
    }
}
