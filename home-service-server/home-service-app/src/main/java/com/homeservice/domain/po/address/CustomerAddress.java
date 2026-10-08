package com.homeservice.domain.po.address;
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
/** customer_address 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "customer_address", autoResultMap = true)
public class CustomerAddress {
    /** 客户地址ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 客户业务ID */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 地址簿联系人 */
    @TableField(value = "contact_name")
    private String contactName;

    /** 地址簿联系电话 */
    @TableField(value = "contact_phone")
    private String contactPhone;

    /** 省代码 */
    @TableField(value = "province_code")
    private String provinceCode;

    /** 省名称快照 */
    @TableField(value = "province_name")
    private String provinceName;

    /** 市代码 */
    @TableField(value = "city_code")
    private String cityCode;

    /** 市名称快照 */
    @TableField(value = "city_name")
    private String cityName;

    /** 区代码 */
    @TableField(value = "district_code")
    private String districtCode;

    /** 区名称快照 */
    @TableField(value = "district_name")
    private String districtName;

    /** 详细地址 */
    @TableField(value = "detail")
    private String detail;

    /** 系统解析经度，未解析时为空 */
    @TableField(value = "longitude")
    private BigDecimal longitude;

    /** 系统解析纬度，未解析时为空 */
    @TableField(value = "latitude")
    private BigDecimal latitude;

    /** 是否为默认地址 */
    @TableField(value = "is_default")
    private Boolean isDefault;

    /** 逻辑删除时间；历史订单使用独立快照 */
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
