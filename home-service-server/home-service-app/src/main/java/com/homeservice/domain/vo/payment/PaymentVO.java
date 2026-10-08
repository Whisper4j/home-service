package com.homeservice.domain.vo.payment;
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


/** 所有金额为正；流水不可修改或删除。模拟支付同步成功或整体失败，不存在真实渠道回调。净收款为 PAYMENT+TOP_UP-PARTIAL_REFUND-FULL_REFUND。 */
@Builder
public record PaymentVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "orderId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long orderId,

    @JsonProperty(value = "businessNo", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 80)
    @NotBlank
    String businessNo,

    @JsonProperty(value = "type", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    PaymentType type,

    @JsonProperty(value = "amount", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiMoney
    @DecimalMin("0.00")
    @DecimalMax("999999999.99")
    @Digits(integer = 9, fraction = 2)
    BigDecimal amount,

    @JsonProperty(value = "createdAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime createdAt
) {}
