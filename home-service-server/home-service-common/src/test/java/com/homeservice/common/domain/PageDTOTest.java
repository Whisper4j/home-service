package com.homeservice.common.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class PageDTOTest {
    @Test void emptyAndOutOfRangePagesPreserveCounts() {
        assertThat(PageDTO.of(List.of(), 0, 20)).isEqualTo(new PageDTO<>(List.of(), 0, 0));
        assertThat(PageDTO.of(List.of(), 41, 20)).isEqualTo(new PageDTO<>(List.of(), 41, 3));
        assertThat(PageDTO.of(List.of("x"), 41, 20).total()).isEqualTo(41);
        assertThatThrownBy(() -> PageDTO.of(List.of(), 1, 0)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void defaultsAndResponseShapeAreStable() {
        assertThat(new PageQuery().getPageNo()).isEqualTo(1);
        assertThat(new PageQuery().getPageSize()).isEqualTo(20);
        assertThat(R.ok("x")).isEqualTo(new R<>("SUCCESS", "成功", "x"));
    }
}
