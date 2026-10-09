package com.homeservice.domain.query.order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfferQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = "搜索关键词长度必须在1到100之间")
    private String keyword; // 搜索关键词
    private LocalDate from; // 开始日期
    private LocalDate to; // 结束日期

    @AssertTrue(message = "from 不得晚于 to")
    @JsonIgnore
    public boolean isValidRange() {
        return from == null || to == null || !from.isAfter(to);
    }
}
