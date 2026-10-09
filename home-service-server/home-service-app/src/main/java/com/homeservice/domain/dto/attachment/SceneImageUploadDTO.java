package com.homeservice.domain.dto.attachment;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import org.springframework.web.multipart.MultipartFile;

import java.time.*;

/**
 * 现场图片上传请求类
 * 接收现场图片上传相关请求参数
 */
@Builder
public record SceneImageUploadDTO(
        @JsonProperty(value = "file", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        MultipartFile file) {}
