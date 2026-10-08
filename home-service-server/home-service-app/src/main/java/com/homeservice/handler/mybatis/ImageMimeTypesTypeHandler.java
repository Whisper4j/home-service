package com.homeservice.handler.mybatis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import java.util.List;
public class ImageMimeTypesTypeHandler extends TypedJsonTypeHandler<List<ImageMimeType>> {
    public ImageMimeTypesTypeHandler() { super(new TypeReference<List<ImageMimeType>>() {}); }
}
