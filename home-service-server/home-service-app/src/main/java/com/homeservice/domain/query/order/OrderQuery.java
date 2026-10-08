package com.homeservice.domain.query.order;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;
import com.homeservice.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


/** 日期按预约开始时间在 Asia/Shanghai 的自然日闭区间筛选；from 不得晚于 to。keyword 搜索订单 ID 或服务名称。 statuses 以逗号分隔传输，可筛选多个精确状态；与 status 互斥。仅用于页面浏览分组，不改变正式状态。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQuery extends com.homeservice.common.domain.PageQuery {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BookingType bookingType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate from;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate to;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 10)
    @UniqueElements
    @Valid
    private List<@NotNull OrderStatus> statuses;

    @AssertTrue(message = "status 与 statuses 互斥") @JsonIgnore
    public boolean isExclusiveStatus() { return status == null || statuses == null; }
    @AssertTrue(message = "from 不得晚于 to") @JsonIgnore
    public boolean isValidRange() { return from == null || to == null || !from.isAfter(to); }
}
