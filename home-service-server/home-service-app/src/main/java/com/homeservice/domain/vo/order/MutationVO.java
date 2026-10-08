package com.homeservice.domain.vo.order;
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


/** 对应 OpenAPI MutationVO；仅定义数据边界，不实现业务。 */
@Builder
public record MutationVO(
    @JsonProperty(value = "success", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @AssertTrue
    Boolean success
) {}
