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


/** 按 createdAt、数值ID升序返回。创建时省略 fromStatus；SYSTEM 不提供 actorId/actorRole。客户及人员不返回内部账号ID，actorRole 仅用于解释操作来源；原因经过权限脱敏。管理员可查看审计所需的操作者ID。状态写入与历史记录同事务完成。 */
@Builder
public record OrderStatusHistoryVO(
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
    OrderStatus fromStatus,

    @JsonProperty(value = "toStatus", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OrderStatus toStatus,

    @JsonProperty(value = "actorType", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    ActorType actorType,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiId
    @Positive
    Long actorId,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    Role actorRole,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Size(min = 1, max = 300)
    String reason,

    @JsonProperty(value = "createdAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime createdAt
) {}
