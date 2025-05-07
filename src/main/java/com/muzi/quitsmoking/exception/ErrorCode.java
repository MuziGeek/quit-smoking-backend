package com.muzi.quitsmoking.exception;

/**
 * 错误码枚举类
 */
public enum ErrorCode {
    /**
     * 成功
     */
    SUCCESS(0, "操作成功"),

    /**
     * 客户端错误
     */
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    
    /**
     * 用户错误
     */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_LOGIN_FAILED(1003, "用户名或密码错误"),
    USER_ACCOUNT_LOCKED(1004, "账户已锁定"),
    USER_ACCOUNT_DISABLED(1005, "账户已禁用"),
    USER_VERIFY_CODE_ERROR(1006, "验证码错误"),
    USER_TOKEN_EXPIRED(1007, "登录已过期"),
    USER_TOKEN_INVALID(1008, "无效的登录状态"),
    
    /**
     * 业务错误
     */
    OPERATION_FAILED(2001, "操作失败"),
    DATA_NOT_FOUND(2002, "数据不存在"),
    DATA_ALREADY_EXISTS(2003, "数据已存在"),
    DATA_ERROR(2004, "数据错误"),
    INSUFFICIENT_POINTS(2005, "积分不足"),
    ALREADY_CHECKED_IN(2006, "今日已打卡"),
    ALREADY_COMPLETED(2007, "已完成"),
    
    /**
     * 系统错误
     */
    SYSTEM_ERROR(5000, "系统错误"),
    INTERNAL_SERVER_ERROR(5001, "服务器内部错误"),
    SERVICE_UNAVAILABLE(5003, "服务不可用"),
    
    /**
     * 外部服务错误
     */
    EXTERNAL_SERVICE_ERROR(6000, "外部服务错误"),
    API_GATEWAY_ERROR(6001, "API网关错误"),
    
    /**
     * 文件上传错误
     */
    FILE_UPLOAD_ERROR(7001, "文件上传失败"),
    FILE_TOO_LARGE(7002, "文件过大"),
    FILE_TYPE_NOT_ALLOWED(7003, "不支持的文件类型"),
    
    /**
     * 自定义错误
     */
    CUSTOM_ERROR(9000, "自定义错误");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
} 