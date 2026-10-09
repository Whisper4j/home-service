package com.homeservice.domain.dto.catalog;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homeservice.enums.CatalogStatus;
import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiIds;
import com.homeservice.handler.json.ApiMoney;
import com.homeservice.validation.MultipleOf;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

import org.hibernate.validator.constraints.UniqueElements;

@Data
public class SkuDTO {

    @NotNull(message = MessageConstant.CATALOG_SELECTION_INVALID)
    @ApiId
    @Positive(message = MessageConstant.CATALOG_SELECTION_INVALID)
    private Long itemId; // 服务项目ID

    @Size(max = 80, message = MessageConstant.NAME_TOO_LONG_80)
    @NotBlank(message = MessageConstant.NAME_REQUIRED)
    private String name; // 名称

    @NotNull(message = MessageConstant.STANDARD_PRICE_REQUIRED)
    @ApiMoney
    @DecimalMin(value = "0.00", message = MessageConstant.AMOUNT_NEGATIVE)
    @DecimalMax(value = "999999999.99", message = MessageConstant.AMOUNT_TOO_LARGE)
    @Digits(integer = 9, fraction = 2, message = MessageConstant.AMOUNT_SCALE_INVALID)
    private BigDecimal standardPrice; // 标准价格

    @NotNull(message = MessageConstant.MINIMUM_OFFER_PRICE_REQUIRED)
    @ApiMoney
    @DecimalMin(value = "0.00", message = MessageConstant.AMOUNT_NEGATIVE)
    @DecimalMax(value = "999999999.99", message = MessageConstant.AMOUNT_TOO_LARGE)
    @Digits(integer = 9, fraction = 2, message = MessageConstant.AMOUNT_SCALE_INVALID)
    private BigDecimal minimumOfferPrice; // 最低优惠价

    @NotNull(message = MessageConstant.DURATION_REQUIRED)
    @Min(value = 30, message = MessageConstant.DURATION_RANGE_INVALID)
    @Max(value = 720, message = MessageConstant.DURATION_RANGE_INVALID)
    @MultipleOf(value = 30, message = MessageConstant.DURATION_STEP_INVALID)
    private Integer durationMinutes; // 服务时长（分钟）

    @Size(max = 20, message = MessageConstant.UNIT_TOO_LONG)
    @NotBlank(message = MessageConstant.UNIT_REQUIRED)
    private String unit; // 计价单位

    @NotNull(message = MessageConstant.SKILL_REQUIRED)
    @Size(min = 1, message = MessageConstant.SKILL_AT_LEAST_ONE)
    @UniqueElements(message = MessageConstant.SKILL_DUPLICATED)
    @ApiIds
    @Valid
    private List<@NotNull(message = MessageConstant.SKILL_INVALID) @Positive(message = MessageConstant.SKILL_INVALID) Long> skillIds; // 技能ID列表

    @NotNull(message = MessageConstant.CATALOG_STATUS_REQUIRED)
    private CatalogStatus status; // 状态

    @NotNull(message = MessageConstant.SUPPORTS_OFFER_REQUIRED)
    private Boolean supportsOffer; // 是否支持优惠

    @Size(max = 2000, message = MessageConstant.DESCRIPTION_TOO_LONG_2000)
    @NotBlank(message = MessageConstant.DESCRIPTION_REQUIRED)
    private String description; // 说明

    @Size(max = 1000, message = MessageConstant.INCLUDED_CONTENT_TOO_LONG)
    @NotBlank(message = MessageConstant.INCLUDED_CONTENT_REQUIRED)
    private String included; // 包含内容

    @Size(max = 1000, message = MessageConstant.EXCLUDED_CONTENT_TOO_LONG)
    @NotBlank(message = MessageConstant.EXCLUDED_CONTENT_REQUIRED)
    private String excluded; // 不含内容

    @NotNull(message = MessageConstant.CUSTOMER_SUPPLIES_PARTS_REQUIRED)
    private Boolean customerSuppliesParts; // 是否客户自备配件

    @JsonProperty(value = "clientEntryCode", required = true)
    @Size(min = 1, max = 64, message = MessageConstant.CLIENT_ENTRY_INVALID)
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$", message = MessageConstant.CLIENT_ENTRY_INVALID)
    private String clientEntryCode; // 客户端入口编码
}
