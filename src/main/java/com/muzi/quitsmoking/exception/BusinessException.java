package com.muzi.quitsmoking.exception;

/**
 * 业务异常类
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
} 