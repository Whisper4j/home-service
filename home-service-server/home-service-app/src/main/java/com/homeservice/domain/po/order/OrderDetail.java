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
/** order_detail 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "order_detail", autoResultMap = true)
public class OrderDetail {
    /** 订单ID，一对一 */
    @TableId(value = "order_id", type = IdType.INPUT)
    private Long orderId;

    /** 创建时分类ID */
    @TableField(value = "category_id")
    private Long categoryId;

    /** 创建时项目ID */
    @TableField(value = "item_id")
    private Long itemId;

    /** 分类名称快照 */
    @TableField(value = "category_name")
    private String categoryName;

    /** 项目名称快照 */
    @TableField(value = "item_name")
    private String itemName;

    /** SKU名称快照 */
    @TableField(value = "sku_name")
    private String skuName;

    /** 业务性质快照 */
    @TableField(value = "service_kind")
    private ServiceKind serviceKind;

    /** 标准价快照 */
    @TableField(value = "standard_price")
    private BigDecimal standardPrice;

    /** 最低优惠报价快照 */
    @TableField(value = "minimum_offer_price")
    private BigDecimal minimumOfferPrice;

    /** 预计时长快照 */
    @TableField(value = "duration_minutes")
    private Integer durationMinutes;

    /** 计价单位快照 */
    @TableField(value = "unit")
    private String unit;

    /** 服务说明快照 */
    @TableField(value = "description")
    private String description;

    /** 包含内容快照 */
    @TableField(value = "included")
    private String included;

    /** 不包含内容快照 */
    @TableField(value = "excluded")
    private String excluded;

    /** 客户自备配件标记快照 */
    @TableField(value = "customer_supplies_parts")
    private Boolean customerSuppliesParts;

    /** 创建时地址簿联系人快照 */
    @TableField(value = "address_contact_name")
    private String addressContactName;

    /** 创建时地址簿电话快照 */
    @TableField(value = "address_contact_phone")
    private String addressContactPhone;

    /** 省代码快照 */
    @TableField(value = "province_code")
    private String provinceCode;

    /** 省名称快照 */
    @TableField(value = "province_name")
    private String provinceName;

    /** 市代码快照 */
    @TableField(value = "city_code")
    private String cityCode;

    /** 市名称快照 */
    @TableField(value = "city_name")
    private String cityName;

    /** 区代码快照 */
    @TableField(value = "district_code")
    private String districtCode;

    /** 区名称快照 */
    @TableField(value = "district_name")
    private String districtName;

    /** 详细地址快照 */
    @TableField(value = "detail")
    private String detail;

    /** 经度快照 */
    @TableField(value = "longitude")
    private BigDecimal longitude;

    /** 纬度快照 */
    @TableField(value = "latitude")
    private BigDecimal latitude;

    /** 创建时是否为默认地址 */
    @TableField(value = "was_default")
    private Boolean wasDefault;

    /** 快照创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
