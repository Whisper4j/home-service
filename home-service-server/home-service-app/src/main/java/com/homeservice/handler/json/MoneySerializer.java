package com.homeservice.handler.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.math.*;

/**
 * 金额序列化器类
 * 将金额转换为JSON输出格式
 */
public class MoneySerializer extends JsonSerializer<BigDecimal> {
    /**
     * 序列化接口输出数据
     */
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value.signum() < 0 || value.compareTo(new BigDecimal("999999999.99")) > 0)
            throw JsonMappingException.from(gen, "金额超出契约范围");
        try {
            gen.writeString(value.setScale(2, RoundingMode.UNNECESSARY).toPlainString());
        } catch (ArithmeticException e) {
            throw JsonMappingException.from(gen, "金额精度超出两位小数");
        }
    }
}
