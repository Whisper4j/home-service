package com.homeservice.common.exception;
public interface ErrorType {
    String code();
    int httpStatus();
    String message();
}
