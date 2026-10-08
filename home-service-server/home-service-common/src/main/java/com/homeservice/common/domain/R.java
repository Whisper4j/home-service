package com.homeservice.common.domain;
import com.fasterxml.jackson.annotation.JsonInclude;
/** HTTP 响应的唯一包裹类型，data 始终存在。 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public record R<T>(String code, String message, T data) {
    public static <T> R<T> ok(T data) { return new R<>("SUCCESS", "成功", data); }
}
