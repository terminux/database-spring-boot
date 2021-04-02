package com.ugrong.framework.database.exceptions;


import org.springframework.http.HttpStatus;

/**
 * 自定义业务异常 对应状态码{@link HttpStatus#BAD_REQUEST}
 */
public class ApiBizException extends ApiException {

    public ApiBizException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public ApiBizException(Object result) {
        super(HttpStatus.BAD_REQUEST, result);
    }

    public ApiBizException(String message) {
        this(message, null);
    }

    public ApiBizException(String message, Object result) {
        super(HttpStatus.BAD_REQUEST.value(), message, result);
    }
}
