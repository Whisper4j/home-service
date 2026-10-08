package com.homeservice.domain.vo.schedule;
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


/** 半小时槽查询返回单个30分钟区间；月历segments返回相邻同类合并区间。区间均左闭右开，时间使用Asia/Shanghai。 */
@Builder
public record SlotVO(
    @JsonProperty(value = "startTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime startTime,

    @JsonProperty(value = "endTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime endTime,

    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    SlotStatus status,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    BookingType bookingType,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiId
    @Positive
    Long assignmentId,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @ApiId
    @Positive
    Long orderId
) {}
