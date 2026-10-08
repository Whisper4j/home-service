package com.homeservice.domain.po.order;
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
/** order_price_history 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "order_price_history", autoResultMap = true)
public class OrderPriceHistory {
    /** 报价变化历史ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 优惠订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 变更前价格 */
    @TableField(value = "previous_price")
    private BigDecimal previousPrice;

    /** 变更后价格 */
    @TableField(value = "new_price")
    private BigDecimal newPrice;

    /** 变更后的报价版本 */
    @TableField(value = "price_version")
    private Integer priceVersion;

    /** 发起调价的客户账号ID */
    @TableField(value = "operator_account_id")
    private Long operatorAccountId;

    /** 变更时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
