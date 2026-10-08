package com.homeservice.domain.po.catalog;
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
/** service_sku_skill 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "service_sku_skill", autoResultMap = true)
public class ServiceSkuSkill {
    /** 关联记录ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 业务取值：SKU ID */
    @TableField(value = "sku_id")
    private Long skuId;

    /** 所需技能ID */
    @TableField(value = "skill_id")
    private Long skillId;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
