package com.homeservice.handler.mybatis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import java.util.List;
public class ReviewTagsTypeHandler extends TypedJsonTypeHandler<List<ReviewTag>> {
    public ReviewTagsTypeHandler() { super(new TypeReference<List<ReviewTag>>() {}); }
}
