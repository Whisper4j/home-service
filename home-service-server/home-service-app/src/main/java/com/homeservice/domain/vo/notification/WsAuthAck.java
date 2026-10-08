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


/** 对应 OpenAPI WsAuthAck；仅定义数据边界，不实现业务。 */
@Builder
public record WsAuthAck(
    @JsonProperty(value = "type", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    WsAuthAckType type,

    @JsonProperty(value = "occurredAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime occurredAt
) {}
