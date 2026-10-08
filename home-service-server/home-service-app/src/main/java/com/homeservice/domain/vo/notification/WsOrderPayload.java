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


/** 无门牌、电话、开始码。仅事件相关且有权限的用户收到；优惠池事件需按人员资格过滤。reason 用于关闭或失败。 */
@Builder
public record WsOrderPayload(
    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OrderStatus status,

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
    @Size(min = 1, max = 300)
    String reason
) {}
