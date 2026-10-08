package com.homeservice.domain.po.attachment;
import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.domain.value.*;
import com.homeservice.handler.mybatis.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/** scene_image 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "scene_image", autoResultMap = true)
public class SceneImage {
    /** 现场图片ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 上传客户业务ID */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 内部对象存储定位，不是公开URL */
    @TableField(value = "storage_key")
    private String storageKey;

    /** 清理元数据后文件内容SHA-256 */
    @TableField(value = "content_sha256")
    private String contentSha256;

    /** image/jpeg、image/png或image/webp */
    @TableField(value = "mime_type")
    private ImageMimeType mimeType;

    /** 文件字节数 */
    @TableField(value = "size_bytes")
    private Long sizeBytes;

    /** 解码宽度像素 */
    @TableField(value = "width_px")
    private Long widthPx;

    /** 解码高度像素 */
    @TableField(value = "height_px")
    private Long heightPx;

    /** 未关联临时图片的逻辑删除时间 */
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;

    /** 上传时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
