package com.homeservice.utils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeservice.common.domain.*;
import java.util.function.Function;
/** 将 MyBatis-Plus 适配留在应用模块，公共分页不依赖 ORM。 */
public final class Pages {
    private Pages() {}
    public static <T> Page<T> request(PageQuery query) { return new Page<>(query.getPageNo(), query.getPageSize()); }
    public static <P,V> PageDTO<V> response(IPage<P> page, Function<P,V> convert) {
        return new PageDTO<>(page.getRecords().stream().map(convert).toList(), page.getTotal(), page.getPages());
    }
}
