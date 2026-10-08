package com.homeservice.domain.dto.order;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;
import com.homeservice.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


/** 只有 WAITING_ACCEPTANCE 且未截止可调价。newPrice 与旧价相差非零 5 元整数倍且在订单快照范围内；涨价需 confirmSimulatedPayment=true。涨价补差或降价退款、报价历史、版本递增在同一事务提交，失败全部回滚。 调价遵守订单 价格范围 与价格范围快照；报价不变不可提交。涨价须明确确认模拟补付，降价明确退款。冲突须保留原报价并展示最新报价，用户重新确认后发起新请求。 */
@Builder
public record ChangeOfferDTO(
    @JsonProperty(value = "newPrice", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiMoney
    @DecimalMin("0.00")
    @DecimalMax("999999999.99")
    @Digits(integer = 9, fraction = 2)
    BigDecimal newPrice,

    @JsonProperty(value = "expectedPrice", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiMoney
    @DecimalMin("0.00")
    @DecimalMax("999999999.99")
    @Digits(integer = 9, fraction = 2)
    BigDecimal expectedPrice,

    @JsonProperty(value = "priceVersion", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(1)
    @Max(2147483647)
    Integer priceVersion,

    @JsonProperty(value = "confirmSimulatedPayment", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Boolean confirmSimulatedPayment
) {}
