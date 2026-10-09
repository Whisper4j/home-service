package com.homeservice.domain.dto.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

/**
 * 创建订单请求类
 * 接收创建订单相关请求参数
 */
@Builder
public record CreateOrderDTO(
        @JsonProperty(value = "skuId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long skuId,
        @JsonProperty(value = "addressId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long addressId,
        @JsonProperty(value = "bookingType", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        BookingType bookingType,
        @JsonProperty(value = "startTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime startTime,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal offerPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 0, max = 300)
        String remark,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 40)
        String contactName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 11)
        @Pattern(regexp = "^1[0-9]{10}$")
        String contactPhone,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 0, max = 3)
        @UniqueElements
        @ApiIds
        @Valid
        List<@NotNull @Positive Long> sceneImageIds) {
    /**
     * 校验联系人姓名和电话是否同时填写
     */
    @AssertTrue(message = "联系人和电话必须同时提供")
    @JsonIgnore
    public boolean isContactPair() {
        return (contactName == null) == (contactPhone == null);
    }

    /**
     * 校验预约开始时间是否按半小时对齐
     */
    @AssertTrue(message = "预约开始时间须半小时对齐")
    @JsonIgnore
    public boolean isAlignedStart() {
        return startTime == null || startTime.getMinute() % 30 == 0 && startTime.getSecond() == 0;
    }

    /**
     * 校验订单类型与报价字段是否匹配
     */
    @AssertTrue(message = "优惠预约必须提供报价，标准预约不可携带优惠报价")
    @JsonIgnore
    public boolean isOfferShape() {
        return bookingType == null || (bookingType == BookingType.OFFER) == (offerPrice != null);
    }
}
