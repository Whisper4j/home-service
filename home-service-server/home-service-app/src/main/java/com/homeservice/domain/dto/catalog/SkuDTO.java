package com.homeservice.domain.dto.catalog;

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
 * 服务规格请求类
 * 接收服务规格相关请求参数
 */
@Builder
public record SkuDTO(
        @JsonProperty(value = "itemId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long itemId,
        @JsonProperty(value = "name", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 80)
        @NotBlank
        String name,
        @JsonProperty(value = "standardPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal standardPrice,
        @JsonProperty(value = "minimumOfferPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal minimumOfferPrice,
        @JsonProperty(value = "durationMinutes", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(30)
        @Max(720)
        @MultipleOf(30)
        Integer durationMinutes,
        @JsonProperty(value = "unit", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 20)
        @NotBlank
        String unit,
        @JsonProperty(value = "skillIds", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 2147483647)
        @UniqueElements
        @ApiIds
        @Valid
        List<@NotNull @Positive Long> skillIds,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        CatalogStatus status,
        @JsonProperty(value = "supportsOffer", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean supportsOffer,
        @JsonProperty(value = "description", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 2000)
        @NotBlank
        String description,
        @JsonProperty(value = "included", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 1000)
        @NotBlank
        String included,
        @JsonProperty(value = "excluded", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 1000)
        @NotBlank
        String excluded,
        @JsonProperty(value = "customerSuppliesParts", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean customerSuppliesParts,
        @JsonProperty(value = "clientEntryCode", required = true)
        @Size(min = 1, max = 64)
        @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$")
        String clientEntryCode) {}
