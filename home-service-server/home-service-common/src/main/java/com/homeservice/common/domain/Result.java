package com.homeservice.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 统一封装接口的业务码、提示信息和响应数据
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private T data;

    /**
     * 创建成功响应结果
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = "SUCCESS";
        result.message = "成功";
        return result;
    }

    /**
     * 创建成功响应结果
     */
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = "SUCCESS";
        result.message = "成功";
        result.data = object;
        return result;
    }

    /**
     * 创建失败响应结果
     */
    public static <T> Result<T> error(String message) {
        return error("ERROR", message);
    }

    /**
     * 创建失败响应结果
     */
    public static <T> Result<T> error(String code, String message) {
        return error(code, message, null);
    }

    /**
     * 创建失败响应结果
     */
    public static <T> Result<T> error(String code, String message, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }
}
