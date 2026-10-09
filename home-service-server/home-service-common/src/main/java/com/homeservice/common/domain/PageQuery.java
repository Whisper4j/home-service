package com.homeservice.common.domain;

import jakarta.validation.constraints.*;

import lombok.Data;

/**
 * 分页查询类
 * 接收并校验通用分页参数
 */
@Data
public class PageQuery {

    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer pageNo = 1;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize = 20;

}
