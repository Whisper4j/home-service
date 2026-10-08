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

import com.homeservice.domain.vo.catalog.SkillVO;
/** 对应 OpenAPI WorkerProfileVO；仅定义数据边界，不实现业务。 */
@Builder
public record WorkerProfileVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "accountId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long accountId,

    @JsonProperty(value = "username", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 32)
    @NotBlank
    String username,

    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    AccountStatus status,

    @JsonProperty(value = "displayName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String displayName,

    @JsonProperty(value = "phone", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 11)
    @NotBlank
    @Pattern(regexp = "^1[0-9]{10}$")
    String phone,

    @JsonProperty(value = "cityCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 6)
    @NotBlank
    String cityCode,

    @JsonProperty(value = "skillIds", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @UniqueElements
    @ApiIds
    @Valid
    List<@NotNull @Positive Long> skillIds,

    @JsonProperty(value = "dispatchEnabled", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Boolean dispatchEnabled,

    @JsonProperty(value = "skills", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Valid
    List<@NotNull @Valid SkillVO> skills,

    @JsonProperty(value = "cityName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String cityName
) {}
