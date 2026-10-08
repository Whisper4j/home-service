package com.homeservice.common.domain;
import jakarta.validation.constraints.*;
import lombok.Data;
/** 不接受客户端任意排序列；排序规则由各业务查询固定。 */
@Data
public class PageQuery {
    @NotNull @Min(1) @Max(Integer.MAX_VALUE)
    private Integer pageNo = 1;
    @NotNull @Min(1) @Max(100)
    private Integer pageSize = 20;
}
