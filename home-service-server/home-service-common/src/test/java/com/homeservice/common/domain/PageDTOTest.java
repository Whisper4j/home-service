package com.homeservice.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 分页请求测试类
 * 验证分页请求相关行为
 */
class PageDTOTest {
    /**
     * 验证empty与Out约束范围分页保留数量场景
     */
    @Test
    void emptyAndOutOfRangePagesPreserveCounts() {
        assertThat(PageDTO.of(List.of(), 0, 20)).isEqualTo(new PageDTO<>(List.of(), 0, 0));
        assertThat(PageDTO.of(List.of(), 41, 20)).isEqualTo(new PageDTO<>(List.of(), 41, 3));
        assertThat(PageDTO.of(List.of("x"), 41, 20).getTotal()).isEqualTo(41);
        assertThatThrownBy(() -> PageDTO.of(List.of(), 1, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * 验证defaults与响应结构AreStable场景
     */
    @Test
    void defaultsAndResponseShapeAreStable() {
        assertThat(new PageQuery().getPageNo()).isEqualTo(1);
        assertThat(new PageQuery().getPageSize()).isEqualTo(20);
        Result<Void> empty = Result.success();
        assertThat(empty.getCode()).isEqualTo("SUCCESS");
        assertThat(empty.getMessage()).isEqualTo("成功");
        assertThat(empty.getData()).isNull();
        assertThat(Result.success("x").getData()).isEqualTo("x");
        Result<Void> simpleError = Result.error("失败");
        assertThat(simpleError.getCode()).isEqualTo("ERROR");
        assertThat(simpleError.getMessage()).isEqualTo("失败");
        assertThat(simpleError.getData()).isNull();
        Result<Void> codedError = Result.error("E001", "失败");
        assertThat(codedError.getCode()).isEqualTo("E001");
        assertThat(codedError.getMessage()).isEqualTo("失败");
        assertThat(codedError.getData()).isNull();
        Result<String> error = Result.error("E001", "失败", "details");
        assertThat(error.getCode()).isEqualTo("E001");
        assertThat(error.getMessage()).isEqualTo("失败");
        assertThat(error.getData()).isEqualTo("details");
    }
}
