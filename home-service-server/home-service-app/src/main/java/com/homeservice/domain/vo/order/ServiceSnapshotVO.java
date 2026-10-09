package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

/**
 * 服务快照响应类
 * 封装服务快照相关响应数据
 */
@Builder
public record ServiceSnapshotVO(
        @JsonProperty(value = "categoryName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 60)
        @NotBlank
        String categoryName,
        @JsonProperty(value = "itemName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 60)
        @NotBlank
        String itemName,
        @JsonProperty(value = "skuName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 80)
        @NotBlank
        String skuName,
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
        @ApiIds
        @Valid
        List<@NotNull @Positive Long> skillIds,
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
        Boolean customerSuppliesParts) {}
