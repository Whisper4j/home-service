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

import com.homeservice.domain.vo.catalog.SkuVO;
/** 返回数据库配置的全部展示入口，按 groupSort、groupCode、sort、code 稳定排序。同组元数据必须一致。已绑定时返回完整 SKU 展示信息；未绑定省略 sku。available=true 必须有 sku；false 必须有 unavailableReason。不得寻找替代 SKU。 */
@Builder
public record ClientEntryVO(
    @JsonProperty(value = "code", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 64)
    @NotBlank
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$")
    String code,

    @JsonProperty(value = "serviceKind", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    ServiceKind serviceKind,

    @JsonProperty(value = "groupCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 64)
    @NotBlank
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$")
    String groupCode,

    @JsonProperty(value = "groupName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 80)
    @NotBlank
    String groupName,

    @JsonProperty(value = "groupDescription", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 0, max = 1000)
    String groupDescription,

    @JsonProperty(value = "groupSort", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Integer groupSort,

    @JsonProperty(value = "name", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 80)
    @NotBlank
    String name,

    @JsonProperty(value = "description", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 0, max = 2000)
    String description,

    @JsonProperty(value = "sort", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Integer sort,

    @JsonProperty(value = "available", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Boolean available,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Size(min = 1, max = 300)
    String unavailableReason,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Valid
    SkuVO sku
) {}
