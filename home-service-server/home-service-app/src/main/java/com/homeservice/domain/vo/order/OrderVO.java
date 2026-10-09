package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.domain.vo.attachment.SceneImageVO;
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
 * 订单响应类
 * 封装订单相关响应数据
 */
@Builder
public record OrderVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "customerId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long customerId,
        @JsonProperty(value = "skuId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long skuId,
        @JsonProperty(value = "bookingType", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        BookingType bookingType,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OrderStatus status,
        @JsonProperty(value = "paymentStatus", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        PaymentStatus paymentStatus,
        @JsonProperty(value = "dispatchStatus", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        DispatchStatus dispatchStatus,
        @JsonProperty(value = "service", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Valid
        ServiceSnapshotVO service,
        @JsonProperty(value = "address", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Valid
        OrderAddressSnapshotVO address,
        @JsonProperty(value = "startTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime startTime,
        @JsonProperty(value = "endTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime endTime,
        @JsonProperty(value = "bufferEndTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime bufferEndTime,
        @JsonProperty(value = "createdAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime createdAt,
        @JsonProperty(value = "paymentDeadline", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime paymentDeadline,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime offerDeadline,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime dispatchDeadline,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime confirmationDeadline,
        @JsonProperty(value = "currentPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal currentPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal dealPrice,
        @JsonProperty(value = "priceVersion", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(2147483647)
        Integer priceVersion,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiId
        @Positive
        Long workerId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 40)
        String workerName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 300)
        String cancellationReason,
        @JsonProperty(value = "remark", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 0, max = 300)
        String remark,
        @JsonProperty(value = "reviewed", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean reviewed,
        @JsonProperty(value = "sceneImages", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Size(min = 0, max = 3)
        @Valid
        List<@NotNull @Valid SceneImageVO> sceneImages,
        @JsonProperty(value = "contactName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String contactName,
        @JsonProperty(value = "contactPhone", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 11)
        @NotBlank
        @Pattern(regexp = "^1[0-9]{10}$")
        String contactPhone,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime offerPublishedAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime closedAt,
        @JsonProperty(value = "allowedActions", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @UniqueElements
        @Valid
        List<@NotNull OrderAction> allowedActions) {}
