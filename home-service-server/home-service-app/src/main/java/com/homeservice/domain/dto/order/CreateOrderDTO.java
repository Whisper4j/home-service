package com.homeservice.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homeservice.enums.BookingType;
import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiIds;
import com.homeservice.handler.json.ApiMoney;
import com.homeservice.handler.json.RejectExplicitNull;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

import org.hibernate.validator.constraints.UniqueElements;

@Data
public class CreateOrderDTO {

    @NotNull(message = "服务规格ID不能为空")
    @ApiId
    @Positive(message = "服务规格ID必须大于0")
    private Long skuId; // 服务规格ID

    @NotNull(message = "地址ID不能为空")
    @ApiId
    @Positive(message = "地址ID必须大于0")
    private Long addressId; // 地址ID

    @NotNull(message = "预约类型不能为空")
    private BookingType bookingType; // 预约类型

    @NotNull(message = "开始时间不能为空")
    private OffsetDateTime startTime; // 开始时间

    @RejectExplicitNull
    @ApiMoney
    @DecimalMin(value = "0.00", message = "offer价格不能小于0.00")
    @DecimalMax(value = "999999999.99", message = "offer价格不能大于999999999.99")
    @Digits(integer = 9, fraction = 2, message = "offer价格精度不正确")
    private BigDecimal offerPrice; // 优惠报价

    @RejectExplicitNull
    @Size(max = 300, message = "备注长度不能超过300")
    private String remark; // 备注

    @RejectExplicitNull
    @Size(min = 1, max = 40, message = "联系人长度必须在1到40之间")
    private String contactName; // 联系人

    @RejectExplicitNull
    @Size(min = 1, max = 11, message = "联系电话长度必须在1到11之间")
    @Pattern(regexp = "^1[0-9]{10}$", message = "联系电话格式不正确")
    private String contactPhone; // 联系电话

    @RejectExplicitNull
    @Size(max = 3, message = "现场图片ID列表数量不能超过3")
    @UniqueElements(message = "现场图片ID列表不能重复")
    @ApiIds
    @Valid
    private List<@NotNull(message = "现场图片ID列表元素不能为空") @Positive(message = "现场图片ID列表元素必须大于0") Long> sceneImageIds; // 现场图片ID列表

    @AssertTrue(message = "联系人和电话必须同时提供")
    @JsonIgnore
    public boolean isContactPair() {
        return (contactName == null) == (contactPhone == null);
    }

    @AssertTrue(message = "预约开始时间须半小时对齐")
    @JsonIgnore
    public boolean isAlignedStart() {
        return startTime == null || startTime.getMinute() % 30 == 0 && startTime.getSecond() == 0;
    }

    @AssertTrue(message = "优惠预约必须提供报价，标准预约不可携带优惠报价")
    @JsonIgnore
    public boolean isOfferShape() {
        return bookingType == null || (bookingType == BookingType.OFFER) == (offerPrice != null);
    }
}
