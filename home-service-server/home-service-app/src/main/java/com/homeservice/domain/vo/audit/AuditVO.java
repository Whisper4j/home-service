package com.homeservice.domain.vo.audit;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 审计响应类
 * 封装审计相关响应数据
 */
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
        Long orderId) {}
