package com.homeservice.common.domain;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PageQuery {

    @NotNull(message = MessageConstant.PAGE_NO_REQUIRED)
    @Min(value = 1, message = MessageConstant.PAGE_NO_INVALID)
    private Integer pageNo = 1; // 页码

    @NotNull(message = MessageConstant.PAGE_SIZE_REQUIRED)
    @Min(value = 1, message = MessageConstant.PAGE_SIZE_INVALID)
    @Max(value = 100, message = MessageConstant.PAGE_SIZE_INVALID)
    private Integer pageSize = 20; // 每页数量

}
