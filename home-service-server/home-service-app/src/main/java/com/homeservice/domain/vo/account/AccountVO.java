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


/** 对应 OpenAPI AccountVO；仅定义数据边界，不实现业务。 */
@Builder
public record AccountVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "username", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 32)
    @NotBlank
    String username,

    @JsonProperty(value = "displayName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String displayName,

    @JsonProperty(value = "role", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Role role,

    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    AccountStatus status,

    @JsonProperty(value = "phone", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 11)
    @NotBlank
    String phone
) {}
