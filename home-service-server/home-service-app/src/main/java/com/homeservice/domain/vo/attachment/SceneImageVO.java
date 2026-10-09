package com.homeservice.domain.vo.attachment;

import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class SceneImageVO {

    @ApiId
    private Long id; // 主键ID

    private String mimeType; // MIME类型

    private Integer size; // 文件字节数

    private OffsetDateTime createdAt; // 创建时间
}
