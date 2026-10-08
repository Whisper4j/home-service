package com.homeservice.handler.mybatis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/** 每个子类给出完整泛型类型，保证数组元素不退化为 LinkedHashMap。SQL NULL 与 [] 独立。 */
public abstract class TypedJsonTypeHandler<T> extends BaseTypeHandler<T> {
    private static final ObjectMapper JSON;
    static {
        var time = new JavaTimeModule();
        var format = DateTimeFormatter.ofPattern("HH:mm");
        time.addSerializer(LocalTime.class, new LocalTimeSerializer(format));
        time.addDeserializer(LocalTime.class, new LocalTimeDeserializer(format));
        JSON = JsonMapper.builder().addModule(time).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();
    }
    private final TypeReference<T> type;
    protected TypedJsonTypeHandler(TypeReference<T> type) { this.type = type; }
    public void setNonNullParameter(PreparedStatement ps, int i, T value, JdbcType jdbcType) throws SQLException {
        try { ps.setString(i, JSON.writeValueAsString(value)); }
        catch (Exception e) { throw new SQLException("JSON 持久化编码失败", e); }
    }
    private T parse(String value) throws SQLException {
        if (value == null) return null;
        try { return JSON.readValue(value, type); }
        catch (Exception e) { throw new SQLException("JSON 持久化解码失败", e); }
    }
    public T getNullableResult(ResultSet rs, String name) throws SQLException { return parse(rs.getString(name)); }
    public T getNullableResult(ResultSet rs, int index) throws SQLException { return parse(rs.getString(index)); }
    public T getNullableResult(CallableStatement cs, int index) throws SQLException { return parse(cs.getString(index)); }
}
