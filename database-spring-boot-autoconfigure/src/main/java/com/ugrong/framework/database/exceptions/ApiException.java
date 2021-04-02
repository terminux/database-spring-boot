package com.ugrong.framework.database.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 */
public class ApiException extends RuntimeException {

    private int code;

    private Object result;

    public ApiException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ApiException(int code) {
        this(code, null);
    }

    public ApiException(String message) {
        this(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public ApiException(HttpStatus status) {
        this(status, null);
    }

    public ApiException(HttpStatus status, Object result) {
        this(status.value(), status.getReasonPhrase(), result);
    }

    public ApiException(int code, String message) {
        this(code, message, null);
    }

    public ApiException(int code, String message, Object result) {
        super(message);
        this.code = code;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
