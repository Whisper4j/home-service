package com.homeservice.common.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PageQuery {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1; // 页码

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量不能小于1")
    @Max(value = 100, message = "每页数量不能超过100")
    private Integer pageSize = 20; // 每页数量

}
