package com.homeservice.domain.po.attachment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("scene_image")
public class SceneImage {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long customerId; // 客户ID
    private String storageKey; // 存储键
    private String contentSha256; // 内容摘要
    private String mimeType; // MIME类型
    private Long sizeBytes; // 文件字节数
    private Long widthPx; // 图片宽度
    private Long heightPx; // 图片高度
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt; // 删除时间（NULL有效）
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
