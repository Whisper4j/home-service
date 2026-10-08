package com.homeservice.handler.mybatis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import java.util.List;
public class RestWeekdaysTypeHandler extends TypedJsonTypeHandler<List<Integer>> {
    public RestWeekdaysTypeHandler() { super(new TypeReference<List<Integer>>() {}); }
}
