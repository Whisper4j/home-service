package com.homeservice.domain.query.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import org.hibernate.validator.constraints.UniqueElements;

import java.time.*;
import java.util.List;

/**
 * 订单查询类
 * 封装订单相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BookingType bookingType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 10)
    @UniqueElements
    @Valid
    private List<@NotNull OrderStatus> statuses;

    /**
     * 校验单状态和多状态参数是否互斥
     */
    @AssertTrue(message = "status 与 statuses 互斥")
    @JsonIgnore
    public boolean isExclusiveStatus() {
        return status == null || statuses == null;
    }

    /**
     * 校验开始值是否早于结束值
     */
    @AssertTrue(message = "from 不得晚于 to")
    @JsonIgnore
    public boolean isValidRange() {
        return from == null || to == null || !from.isAfter(to);
    }
}
