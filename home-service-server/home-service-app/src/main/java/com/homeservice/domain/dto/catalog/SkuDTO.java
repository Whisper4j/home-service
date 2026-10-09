package com.homeservice.domain.dto.catalog;

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

    @NotNull(message = "服务项目ID不能为空")
    @ApiId
    @Positive(message = "服务项目ID必须大于0")
    private Long itemId; // 服务项目ID

    @Size(max = 80, message = "名称长度不能超过80")
    @NotBlank(message = "名称不能为空")
    private String name; // 名称

    @NotNull(message = "标准价格不能为空")
    @ApiMoney
    @DecimalMin(value = "0.00", message = "标准价格不能小于0.00")
    @DecimalMax(value = "999999999.99", message = "标准价格不能大于999999999.99")
    @Digits(integer = 9, fraction = 2, message = "标准价格精度不正确")
    private BigDecimal standardPrice; // 标准价格

    @NotNull(message = "最低优惠价不能为空")
    @ApiMoney
    @DecimalMin(value = "0.00", message = "最低优惠价不能小于0.00")
    @DecimalMax(value = "999999999.99", message = "最低优惠价不能大于999999999.99")
    @Digits(integer = 9, fraction = 2, message = "最低优惠价精度不正确")
    private BigDecimal minimumOfferPrice; // 最低优惠价

    @NotNull(message = "服务时长（分钟）不能为空")
    @Min(value = 30, message = "服务时长（分钟）不能小于30")
    @Max(value = 720, message = "服务时长（分钟）不能大于720")
    @MultipleOf(value = 30, message = "服务时长（分钟）必须是30的整数倍")
    private Integer durationMinutes; // 服务时长（分钟）

    @Size(max = 20, message = "计价单位长度不能超过20")
    @NotBlank(message = "计价单位不能为空")
    private String unit; // 计价单位

    @NotNull(message = "技能ID列表不能为空")
    @Size(min = 1, message = "技能ID列表至少包含1项")
    @UniqueElements(message = "技能ID列表不能重复")
    @ApiIds
    @Valid
    private List<@NotNull(message = "技能ID列表元素不能为空") @Positive(message = "技能ID列表元素必须大于0") Long> skillIds; // 技能ID列表

    @NotNull(message = "状态不能为空")
    private CatalogStatus status; // 状态

    @NotNull(message = "是否支持优惠不能为空")
    private Boolean supportsOffer; // 是否支持优惠

    @Size(max = 2000, message = "说明长度不能超过2000")
    @NotBlank(message = "说明不能为空")
    private String description; // 说明

    @Size(max = 1000, message = "包含内容长度不能超过1000")
    @NotBlank(message = "包含内容不能为空")
    private String included; // 包含内容

    @Size(max = 1000, message = "不含内容长度不能超过1000")
    @NotBlank(message = "不含内容不能为空")
    private String excluded; // 不含内容

    @NotNull(message = "是否客户自备配件不能为空")
    private Boolean customerSuppliesParts; // 是否客户自备配件

    @JsonProperty(value = "clientEntryCode", required = true)
    @Size(min = 1, max = 64, message = "客户端入口编码长度必须在1到64之间")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$", message = "客户端入口编码格式不正确")
    private String clientEntryCode; // 客户端入口编码
}
