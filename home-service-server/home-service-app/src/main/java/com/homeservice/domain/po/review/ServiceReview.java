package com.homeservice.domain.po.review;
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
/** service_review 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "service_review", autoResultMap = true)
public class ServiceReview {
    /** 评价ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 已完成订单ID，每单最多一次 */
    @TableField(value = "order_id")
    private Long orderId;

    /** 评价客户业务ID */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 被评价服务人员业务ID */
    @TableField(value = "worker_id")
    private Long workerId;

    /** 评分1至5 */
    @TableField(value = "score")
    private Integer score;

    /** 评价内容 */
    @TableField(value = "content")
    private String content;

    /** 评价标签数组；无标签时保存空数组 */
    @TableField(value = "tags", typeHandler = ReviewTagsTypeHandler.class)
    private List<ReviewTag> tags;

    /** 评价时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
