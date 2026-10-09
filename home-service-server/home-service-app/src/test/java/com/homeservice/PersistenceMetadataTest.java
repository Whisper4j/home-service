package com.homeservice;

import static org.assertj.core.api.Assertions.*;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.homeservice.domain.po.idempotency.HttpIdempotencyRecord;
import com.homeservice.support.SqlBaseline;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 持久化元数据测试类
 * 验证持久化元数据相关行为
 */
class PersistenceMetadataTest {
    /**
     * 验证allTablesColumnsPrimaryKeysEnums与Generated字段MatchSQL场景
     */
    @Test
    void allTablesColumnsPrimaryKeysEnumsAndGeneratedColumnMatchSql() throws Exception {
        var tables = SqlBaseline.read();
        var entities = SqlBaseline.entities();
        assertThat(entities.keySet()).isEqualTo(tables.keySet());
        assertThat(entities).hasSize(27);
        MybatisConfiguration configuration = new MybatisConfiguration();
        for (var entry : entities.entrySet()) {
            Class<?> type = entry.getValue();
            var expected = tables.get(entry.getKey());
            Class<?> mapper =
                    Class.forName(type.getName().replace(".domain.po.", ".mapper.") + "Mapper");
            configuration.addMapper(mapper);
            var metadata = TableInfoHelper.getTableInfo(type);
            assertThat(metadata.getKeyColumn()).isEqualTo(expected.primaryKey());
            assertThat(metadata.getIdType())
                    .isEqualTo(
                            expected.columns().get(expected.primaryKey()).auto()
                                    ? IdType.AUTO
                                    : IdType.INPUT);
            Set<String> columns = new HashSet<>();
            columns.add(metadata.getKeyColumn());
            metadata.getFieldList().forEach(f -> columns.add(f.getColumn()));
            assertThat(columns).isEqualTo(expected.columns().keySet());
            assertThat(metadata.isWithLogicDelete())
                    .isEqualTo(expected.columns().containsKey("deleted_at"));
            assertThat(type.getAnnotation(TableName.class).autoResultMap()).isTrue();
            for (Field field : type.getDeclaredFields())
                if (field.getType().isEnum()) {
                    assertThat(
                                    field.getType()
                                            .getDeclaredField("value")
                                            .isAnnotationPresent(EnumValue.class))
                            .isTrue();
                }
            assertThat(configuration.hasStatement(mapper.getName() + ".selectById")).isTrue();
        }
        Field generated = HttpIdempotencyRecord.class.getDeclaredField("scopeIdentity");
        assertThat(generated.getAnnotation(TableField.class).insertStrategy())
                .isEqualTo(FieldStrategy.NEVER);
        assertThat(generated.getAnnotation(TableField.class).updateStrategy())
                .isEqualTo(FieldStrategy.NEVER);
        assertThat(tables.values().stream().mapToInt(t -> t.columns().size()).sum()).isEqualTo(305);
    }
}
