package com.homeservice.handler.mybatis;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.*;
@Component @RequiredArgsConstructor
public class TimestampFillHandler implements MetaObjectHandler {
    private final Clock clock;
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Asia/Shanghai")).withNano(0);
        strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
    }
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updatedAt")) setFieldValByName("updatedAt",
            LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Asia/Shanghai")).withNano(0), metaObject);
    }
}
