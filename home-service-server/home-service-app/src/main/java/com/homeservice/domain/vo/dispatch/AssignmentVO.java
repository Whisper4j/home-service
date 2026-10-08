package com.homeservice.domain.vo.dispatch;
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


/** ACTIVE：assignedAt 必需，不返回 releasedAt/releaseReason/finishedAt。RELEASED：取消释放时 releasedAt、releaseReason 必需，省略 finishedAt。FINISHED：履约完成确认后 finishedAt 必需，省略释放字段。时间和历史不可被后续目录或排班修改覆盖。 */
@Builder
public record AssignmentVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "orderId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long orderId,

    @JsonProperty(value = "workerId", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long workerId,

    @JsonProperty(value = "workerName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String workerName,

    @JsonProperty(value = "bookingType", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    BookingType bookingType,

    @JsonProperty(value = "status", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    AssignmentStatus status,

    @JsonProperty(value = "assignedAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime assignedAt,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    OffsetDateTime releasedAt,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    @Size(min = 1, max = 300)
    String releaseReason,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RejectExplicitNull
    OffsetDateTime finishedAt
) {}
