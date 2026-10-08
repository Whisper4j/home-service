package com.homeservice.domain.query.schedule;
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


/** 对应 OpenAPI WorkerMonthQuery；仅定义数据边界，不实现业务。 */
@Data
public class WorkerMonthQuery {
    @NotNull
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$")
    private String month;
}
