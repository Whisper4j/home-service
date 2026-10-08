package com.homeservice.domain.vo.catalog;
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


/** 对应 OpenAPI CategoryVO；仅定义数据边界，不实现业务。 */
@Builder
public record CategoryVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "name", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 60)
    @NotBlank
    String name,

    @JsonProperty(value = "sort", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(0)
    @Max(9999)
    Integer sort,

    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    CatalogStatus status
) {}
