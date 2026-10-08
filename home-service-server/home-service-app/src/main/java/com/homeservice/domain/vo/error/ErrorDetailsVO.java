package com.homeservice.domain.vo.error;
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

import com.homeservice.domain.vo.error.FieldErrorVO;
/** PRICE_CHANGED 必须携带 currentPrice 和 priceVersion；客户端展示变化并重新查询，不能自动重试抢单或调价。 */
@Builder
public record ErrorDetailsVO(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Valid
    List<@NotNull @Valid FieldErrorVO> fieldErrors,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiMoney
    @DecimalMin("0.00")
    @DecimalMax("999999999.99")
    @Digits(integer = 9, fraction = 2)
    BigDecimal currentPrice,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Min(1)
    @Max(2147483647)
    Integer priceVersion,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    OrderStatus currentStatus,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @UniqueElements
    @ApiIds
    @Valid
    List<@NotNull @Positive Long> conflictingOrderIds
) {}
