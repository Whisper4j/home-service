package com.homeservice.domain.po.attachment;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 现场图片持久化类
 * 映射scene_image表数据
 */
@Data
@TableName(value = "scene_image", autoResultMap = true)
public class SceneImage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "customer_id")
    private Long customerId;
    @TableField(value = "storage_key")
    private String storageKey;
    @TableField(value = "content_sha256")
    private String contentSha256;
    @TableField(value = "mime_type")
    private ImageMimeType mimeType;
    @TableField(value = "size_bytes")
    private Long sizeBytes;
    @TableField(value = "width_px")
    private Long widthPx;
    @TableField(value = "height_px")
    private Long heightPx;
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
