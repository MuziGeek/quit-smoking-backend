package com.muzi.quitsmoking.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功
     * @param data 数据
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 错误
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> error(String message) {
        return error(400, message);
    }

    /**
     * 错误
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
} 