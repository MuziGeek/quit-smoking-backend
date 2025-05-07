package com.muzi.quitsmoking.exception;

/**
 * 自定义异常类
 */
public class CustomException extends BaseException {
    public CustomException(String message) {
        super(ErrorCode.CUSTOM_ERROR, message);
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
} 