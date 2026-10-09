package com.homeservice.domain.vo.error;

import lombok.Data;

@Data
public class FieldErrorVO {

    private String field; // 字段名

    private String message; // 提示信息
}
