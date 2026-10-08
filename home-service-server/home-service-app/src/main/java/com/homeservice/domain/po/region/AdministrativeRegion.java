package com.homeservice.domain.po.region;
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
/** administrative_region 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "administrative_region", autoResultMap = true)
public class AdministrativeRegion {
    /** 国家统计局行政区划代码 */
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    /** 上级行政区代码 */
    @TableField(value = "parent_code")
    private String parentCode;

    /** 行政区名称 */
    @TableField(value = "name")
    private String name;

    /** 级别：PROVINCE/CITY/DISTRICT */
    @TableField(value = "level")
    private RegionLevel level;

    /** 是否属于当前服务区域 */
    @TableField(value = "service_enabled")
    private Boolean serviceEnabled;

    /** 展示顺序 */
    @TableField(value = "sort_no")
    private Integer sortNo;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
