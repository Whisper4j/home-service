package com.homeservice.domain.dto.catalog;
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


/** 业务性质独立于可维护分类名称和ID。当前只有 CLEANING 可支持优惠，REPAIR 和 OTHER 仅标准；有支持优惠的SKU时不能改为非清洁。新增分类无需新增订单流程。 */
@Builder
public record ServiceItemDTO(
    @JsonProperty(value = "categoryId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long categoryId,

    @JsonProperty(value = "name", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 60)
    @NotBlank
    String name,

    @JsonProperty(value = "serviceKind", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    ServiceKind serviceKind,

    @JsonProperty(value = "description", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 1000)
    @NotBlank
    String description,

    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    CatalogStatus status
) {}
