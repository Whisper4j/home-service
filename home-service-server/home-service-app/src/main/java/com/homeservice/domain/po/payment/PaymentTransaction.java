package com.homeservice.domain.po.payment;
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
/** payment_transaction 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "payment_transaction", autoResultMap = true)
public class PaymentTransaction {
    /** 模拟资金流水ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 幂等业务号，全局唯一 */
    @TableField(value = "business_no")
    private String businessNo;

    /** 流水类型：PAYMENT/TOP_UP/PARTIAL_REFUND/FULL_REFUND */
    @TableField(value = "type")
    private PaymentType type;

    /** 正金额；方向由流水类型决定 */
    @TableField(value = "amount")
    private BigDecimal amount;

    /** 补差或调价退款对应报价版本 */
    @TableField(value = "related_price_version")
    private Integer relatedPriceVersion;

    /** 流水说明 */
    @TableField(value = "remark")
    private String remark;

    /** 发生时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
