package com.homeservice.domain.dto.order;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.handler.json.ApiMoney;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChangeOfferDTO {

    @NotNull(message = MessageConstant.OFFER_PRICE_REQUIRED)
    @ApiMoney
    @DecimalMin(value = "0.00", message = MessageConstant.AMOUNT_NEGATIVE)
    @DecimalMax(value = "999999999.99", message = MessageConstant.AMOUNT_TOO_LARGE)
    @Digits(integer = 9, fraction = 2, message = MessageConstant.AMOUNT_SCALE_INVALID)
    private BigDecimal newPrice; // 新优惠价

    @NotNull(message = MessageConstant.OFFER_CONTEXT_EXPIRED)
    @ApiMoney
    @DecimalMin(value = "0.00", message = MessageConstant.OFFER_CONTEXT_EXPIRED)
    @DecimalMax(value = "999999999.99", message = MessageConstant.OFFER_CONTEXT_EXPIRED)
    @Digits(integer = 9, fraction = 2, message = MessageConstant.OFFER_CONTEXT_EXPIRED)
    private BigDecimal expectedPrice; // 确认价格

    @NotNull(message = MessageConstant.OFFER_CONTEXT_EXPIRED)
    @Min(value = 1, message = MessageConstant.OFFER_CONTEXT_EXPIRED)
    private Integer priceVersion; // 价格版本

    @NotNull(message = MessageConstant.PAYMENT_CONFIRMATION_REQUIRED)
    private Boolean confirmSimulatedPayment; // 是否确认模拟支付
}
