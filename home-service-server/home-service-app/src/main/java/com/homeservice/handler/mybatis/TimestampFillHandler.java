package com.homeservice.handler.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.RequiredArgsConstructor;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.*;

/**
 * 时间戳填充处理器类
 * 自动填充新增和更新记录的时间戳
 */
@Component
@RequiredArgsConstructor
public class TimestampFillHandler implements MetaObjectHandler {

    private final Clock clock;

    /**
     * 填充新增记录的创建和更新时间
     */
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now =
                LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Asia/Shanghai")).withNano(0);
        strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
    }

    /**
     * 填充记录的更新时间
     */
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updatedAt"))
            setFieldValByName(
                    "updatedAt",
                    LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Asia/Shanghai"))
                            .withNano(0),
                    metaObject);
    }
}
