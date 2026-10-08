package com.homeservice.domain.vo.statistics;
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


/** Asia/Shanghai：今日待服务为预约日期今天且状态PENDING_SERVICE；月及累计统计当前人员全部COMPLETED订单，以closedAt完成确认时间归属月份（含24小时自动确认）。时长累加已完成订单预约服务时长，不是实际工时；不统计收入。独立聚合，不受列表分页影响。 */
@Builder
public record WorkerStatisticsVO(
    @JsonProperty(value = "asOf", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime asOf,

    @JsonProperty(value = "today", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    LocalDate today,

    @JsonProperty(value = "month", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$")
    String month,

    @JsonProperty(value = "todayPendingCount", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(0)
    @Max(2147483647)
    Integer todayPendingCount,

    @JsonProperty(value = "monthCompletedCount", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(0)
    @Max(2147483647)
    Integer monthCompletedCount,

    @JsonProperty(value = "totalCompletedCount", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(0)
    @Max(2147483647)
    Integer totalCompletedCount,

    @JsonProperty(value = "monthBookedMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(0)
    @Max(2147483647)
    Integer monthBookedMinutes,

    @JsonProperty(value = "totalBookedMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(0)
    @Max(2147483647)
    Integer totalBookedMinutes
) {}
