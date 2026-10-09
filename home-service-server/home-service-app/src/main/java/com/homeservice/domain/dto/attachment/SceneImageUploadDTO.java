package com.homeservice.domain.dto.attachment;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class SceneImageUploadDTO {

    @NotNull(message = "上传文件不能为空")
    private MultipartFile file; // 上传文件
}
