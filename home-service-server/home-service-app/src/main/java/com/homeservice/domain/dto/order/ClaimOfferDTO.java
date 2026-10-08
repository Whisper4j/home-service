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


/** 对应 OpenAPI ClaimOfferDTO；仅定义数据边界，不实现业务。 */
@Builder
public record ClaimOfferDTO(
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
    Integer priceVersion
) {}
