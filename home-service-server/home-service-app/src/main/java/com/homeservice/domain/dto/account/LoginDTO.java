package com.homeservice.domain.dto.account;
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


/** 对应 OpenAPI LoginDTO；仅定义数据边界，不实现业务。 */
@Builder
public record LoginDTO(
    @JsonProperty(value = "username", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 32)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,31}$")
    String username,

    @JsonProperty(value = "password", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 8, max = 72)
    @Utf8Password
    @JsonDeserialize(using = PasswordDeserializer.class)
    String password
) { @Override public String toString() { return "LoginDTO[credentials=REDACTED]"; } }
