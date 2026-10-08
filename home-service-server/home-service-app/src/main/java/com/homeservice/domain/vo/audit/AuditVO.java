package com.homeservice.domain.vo.audit;
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


/** USER操作必须包含actorId；SYSTEM定时任务省略actorId。退款处理包含订单ID、退款类型与金额，不能伪造系统的用户身份。 targetType 与 targetId 唯一表达目标类型和标识。订单相关操作必须提供 orderId；按订单查询仅匹配 orderId，不猜测 targetId。 */
@Builder
public record AuditVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "actorType", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    ActorType actorType,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiId
    @Positive
    Long actorId,

    @JsonProperty(value = "action", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    String action,

    @JsonProperty(value = "targetId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long targetId,

    @JsonProperty(value = "detail", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 2000)
    @NotBlank
    String detail,

    @JsonProperty(value = "createdAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime createdAt,

    @JsonProperty(value = "targetType", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    AuditTargetType targetType,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiId
    @Positive
    Long orderId
) {}
