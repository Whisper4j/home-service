package com.homeservice.domain.vo.order;
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

import com.homeservice.domain.vo.dispatch.AssignmentVO;
import com.homeservice.domain.vo.order.OrderStatusHistoryVO;
import com.homeservice.domain.vo.order.PriceHistoryVO;
import com.homeservice.domain.vo.payment.PaymentVO;
import com.homeservice.domain.vo.review.ReviewVO;
/** 对应 OpenAPI OrderHistoryVO；仅定义数据边界，不实现业务。 */
@Builder
public record OrderHistoryVO(
    @JsonProperty(value = "payments", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Valid
    List<@NotNull @Valid PaymentVO> payments,

    @JsonProperty(value = "priceHistory", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Valid
    List<@NotNull @Valid PriceHistoryVO> priceHistory,

    @JsonProperty(value = "assignments", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Valid
    List<@NotNull @Valid AssignmentVO> assignments,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Valid
    ReviewVO review,

    @JsonProperty(value = "statusHistory", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Valid
    List<@NotNull @Valid OrderStatusHistoryVO> statusHistory
) {}
