package com.homeservice.domain.dto.review;
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


/** 对应 OpenAPI ReviewDTO；仅定义数据边界，不实现业务。 */
@Builder
public record ReviewDTO(
    @JsonProperty(value = "score", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(1)
    @Max(5)
    Integer score,

    @JsonProperty(value = "tags", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Size(min = 0, max = 3)
    @UniqueElements
    @Valid
    List<@NotNull ReviewTag> tags,

    @JsonProperty(value = "content", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 0, max = 500)
    String content
) {}
