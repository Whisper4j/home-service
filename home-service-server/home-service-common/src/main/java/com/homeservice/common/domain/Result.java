package com.homeservice.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化版本
    private String code; // 业务码
    private String message; // 提示信息
    private T data; // 响应数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = "SUCCESS";
        result.message = "成功";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = "SUCCESS";
        result.message = "成功";
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String message) {
        return error("ERROR", message);
    }

    public static <T> Result<T> error(String code, String message) {
        return error(code, message, null);
    }

    public static <T> Result<T> error(String code, String message, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }
}
