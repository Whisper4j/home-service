package com.homeservice.domain.query.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 时间槽查询类
 * 封装时间槽相关查询条件
 */
@Data
public class SlotQuery {

    @NotNull
    private LocalDate date;

}
