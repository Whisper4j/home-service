package com.homeservice.domain.vo.account;
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

import com.homeservice.domain.vo.account.AccountVO;
/** 对应 OpenAPI LoginVO；仅定义数据边界，不实现业务。 */
@Builder
public record LoginVO(
    @JsonProperty(value = "accessToken", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 2048)
    @NotBlank
    String accessToken,

    @JsonProperty(value = "tokenType", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    TokenType tokenType,

    @JsonProperty(value = "expiresAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime expiresAt,

    @JsonProperty(value = "account", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Valid
    AccountVO account
) { @Override public String toString() { return "LoginVO[credentials=REDACTED]"; } }
