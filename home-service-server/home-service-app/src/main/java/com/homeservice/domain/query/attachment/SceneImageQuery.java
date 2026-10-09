package com.homeservice.domain.query.attachment;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 现场图片查询类
 * 封装现场图片相关查询条件
 */
@Data
public class SceneImageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    @Positive
    private Long orderId;

}
