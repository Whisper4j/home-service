package com.homeservice.domain.dto.order;

import com.homeservice.handler.json.ApiMoney;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClaimOfferDTO {

    @NotNull(message = "确认价格不能为空")
    @ApiMoney
    @DecimalMin(value = "0.00", message = "确认价格不能小于0.00")
    @DecimalMax(value = "999999999.99", message = "确认价格不能大于999999999.99")
    @Digits(integer = 9, fraction = 2, message = "确认价格精度不正确")
    private BigDecimal expectedPrice; // 确认价格

    @NotNull(message = "价格版本不能为空")
    @Min(value = 1, message = "价格版本不能小于1")
    private Integer priceVersion; // 价格版本
}
