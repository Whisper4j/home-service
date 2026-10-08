package com.homeservice.domain.vo.dispatch;
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


/** 对应 OpenAPI DispatchAttemptVO；仅定义数据边界，不实现业务。 */
@Builder
public record DispatchAttemptVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "orderId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long orderId,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiId
    @Positive
    Long workerId,

    @JsonProperty(value = "result", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    DispatchAttemptResult result,

    @JsonProperty(value = "reason", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 300)
    @NotBlank
    String reason,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Min(0)
    @Max(2147483647)
    Integer serviceMinutes,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Min(0)
    @Max(2147483647)
    Integer orderCount,

    @JsonProperty(value = "createdAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime createdAt
) {}
