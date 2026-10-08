package com.homeservice.handler.mybatis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import java.util.List;
public class WorkIntervalsTypeHandler extends TypedJsonTypeHandler<List<WorkIntervalValue>> {
    public WorkIntervalsTypeHandler() { super(new TypeReference<List<WorkIntervalValue>>() {}); }
}
