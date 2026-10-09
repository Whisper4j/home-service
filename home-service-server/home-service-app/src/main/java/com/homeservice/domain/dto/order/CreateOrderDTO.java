package com.homeservice.domain.dto.order;

import com.homeservice.common.constant.MessageConstant;

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

    @NotNull(message = MessageConstant.ORDER_SELECTION_INVALID)
    @ApiId
    @Positive(message = MessageConstant.ORDER_SELECTION_INVALID)
    private Long skuId; // 服务规格ID

    @NotNull(message = MessageConstant.ORDER_SELECTION_INVALID)
    @ApiId
    @Positive(message = MessageConstant.ORDER_SELECTION_INVALID)
    private Long addressId; // 地址ID

    @NotNull(message = MessageConstant.BOOKING_TYPE_REQUIRED)
    private BookingType bookingType; // 预约类型

    @NotNull(message = MessageConstant.BOOKING_START_TIME_REQUIRED)
    private OffsetDateTime startTime; // 开始时间

    @RejectExplicitNull
    @ApiMoney
    @DecimalMin(value = "0.00", message = MessageConstant.AMOUNT_NEGATIVE)
    @DecimalMax(value = "999999999.99", message = MessageConstant.AMOUNT_TOO_LARGE)
    @Digits(integer = 9, fraction = 2, message = MessageConstant.AMOUNT_SCALE_INVALID)
    private BigDecimal offerPrice; // 优惠报价

    @RejectExplicitNull
    @Size(max = 300, message = MessageConstant.REMARK_TOO_LONG)
    private String remark; // 备注

    @RejectExplicitNull
    @Size(min = 1, max = 40, message = MessageConstant.CONTACT_NAME_TOO_LONG)
    private String contactName; // 联系人

    @RejectExplicitNull
    @Size(min = 1, max = 11, message = MessageConstant.CONTACT_PHONE_INVALID)
    @Pattern(regexp = "^1[0-9]{10}$", message = MessageConstant.CONTACT_PHONE_INVALID)
    private String contactPhone; // 联系电话

    @RejectExplicitNull
    @Size(max = 3, message = MessageConstant.SCENE_IMAGE_TOO_MANY)
    @UniqueElements(message = MessageConstant.SCENE_IMAGE_DUPLICATED)
    @ApiIds
    @Valid
    private List<@NotNull(message = MessageConstant.SCENE_IMAGE_INVALID) @Positive(message = MessageConstant.SCENE_IMAGE_INVALID) Long> sceneImageIds; // 现场图片ID列表

    @AssertTrue(message = MessageConstant.ORDER_CONTACT_INVALID)
    @JsonIgnore
    public boolean isContactPair() {
        return (contactName == null) == (contactPhone == null);
    }

    @AssertTrue(message = MessageConstant.BOOKING_START_TIME_INVALID)
    @JsonIgnore
    public boolean isAlignedStart() {
        return startTime == null || startTime.getMinute() % 30 == 0 && startTime.getSecond() == 0;
    }

    @AssertTrue(message = MessageConstant.OFFER_PRICE_RULE_INVALID)
    @JsonIgnore
    public boolean isOfferShape() {
        return bookingType == null || (bookingType == BookingType.OFFER) == (offerPrice != null);
    }
}
