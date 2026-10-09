package com.homeservice.domain.dto.attachment;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class SceneImageUploadDTO {

    @NotNull(message = MessageConstant.UPLOAD_FILE_REQUIRED)
    private MultipartFile file; // 上传文件
}
