package com.muzi.quitsmoking.exception;

/**
 * 基础异常类
 */
public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    public BaseException(String message) {
        this(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public BaseException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), message);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 