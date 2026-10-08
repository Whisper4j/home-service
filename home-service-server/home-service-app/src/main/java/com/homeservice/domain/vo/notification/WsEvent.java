package com.homeservice.domain.vo.notification;
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

import com.homeservice.domain.vo.notification.WsOrderPayload;
/** 同源 /ws，仅通知，事务提交后发送。OFFER_CREATED=新优惠单；OFFER_PRICE_CHANGED=变价；ORDER_CLAIMED=被抢；ORDER_CLOSED=取消/超时；DISPATCH_SUCCEEDED/FAILED=标准调度结果；ORDER_STATUS_CHANGED=履约变化。客户收本人订单；人员收本人分配及有资格的池事件；管理员收管理事件。允许丢失/重复/乱序，eventId 去重，priceVersion 不得回退；断线、重连、重新登录必须 HTTP 重查，不以推送作为状态裁决。 */
@Builder
public record WsEvent(
    @JsonProperty(value = "eventId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long eventId,

    @JsonProperty(value = "type", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    WsEventType type,

    @JsonProperty(value = "orderId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long orderId,

    @JsonProperty(value = "priceVersion", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(1)
    @Max(2147483647)
    Integer priceVersion,

    @JsonProperty(value = "occurredAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime occurredAt,

    @JsonProperty(value = "payload", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Valid
    WsOrderPayload payload
) {}
