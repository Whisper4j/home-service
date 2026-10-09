package com.homeservice.handler.mybatis;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 强类型JSON处理器类
 * 在MySQL JSON字段和明确Java类型之间转换
 */
public abstract class TypedJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private static final ObjectMapper JSON;

    static {
        var time = new JavaTimeModule();
        var format = DateTimeFormatter.ofPattern("HH:mm");
        time.addSerializer(LocalTime.class, new LocalTimeSerializer(format));
        time.addDeserializer(LocalTime.class, new LocalTimeDeserializer(format));
        JSON =
                JsonMapper.builder()
                        .addModule(time)
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .build();
    }

    private final TypeReference<T> type;

    /**
     * 创建强类型JSON类型Handler实例
     */
    protected TypedJsonTypeHandler(TypeReference<T> type) {
        this.type = type;
    }

    /**
     * 写入非空JSON参数
     */
    public void setNonNullParameter(PreparedStatement ps, int i, T value, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, JSON.writeValueAsString(value));
        } catch (Exception e) {
            throw new SQLException(MessageConstant.JSON_WRITE_FAILED, e);
        }
    }

    /**
     * 解析并校验输入数据
     */
    private T parse(String value) throws SQLException {
        if (value == null) return null;
        try {
            return JSON.readValue(value, type);
        } catch (Exception e) {
            throw new SQLException(MessageConstant.JSON_READ_FAILED, e);
        }
    }

    /**
     * 读取可空JSON查询结果
     */
    public T getNullableResult(ResultSet rs, String name) throws SQLException {
        return parse(rs.getString(name));
    }

    /**
     * 读取可空JSON查询结果
     */
    public T getNullableResult(ResultSet rs, int index) throws SQLException {
        return parse(rs.getString(index));
    }

    /**
     * 读取可空JSON查询结果
     */
    public T getNullableResult(CallableStatement cs, int index) throws SQLException {
        return parse(cs.getString(index));
    }
}
