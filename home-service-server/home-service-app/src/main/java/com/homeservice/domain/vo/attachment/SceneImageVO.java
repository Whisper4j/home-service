package com.homeservice.domain.vo.attachment;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 现场图片响应类
 * 封装现场图片相关响应数据
 */
@Builder
public record SceneImageVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "mimeType", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        ImageMimeType mimeType,
        @JsonProperty(value = "size", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(5242880)
        Integer size,
        @JsonProperty(value = "createdAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime createdAt) {}
