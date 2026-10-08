package com.homeservice.handler.mybatis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import java.util.List;
public class IdempotencyResponseTypeHandler extends TypedJsonTypeHandler<IdempotencyResponse> {
    public IdempotencyResponseTypeHandler() { super(new TypeReference<IdempotencyResponse>() {}); }
}
