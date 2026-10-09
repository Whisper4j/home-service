package com.homeservice.domain.query.attachment;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 订单现场图片查询类
 * 封装订单现场图片相关查询条件
 */
@Data
public class OrderSceneImageQuery {

    @NotNull
    @ApiId
    @Positive
    private Long orderId;

}
