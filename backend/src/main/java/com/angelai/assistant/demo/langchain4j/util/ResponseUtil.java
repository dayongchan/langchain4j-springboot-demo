package com.angelai.assistant.demo.langchain4j.util;

import com.angelai.assistant.demo.langchain4j.dto.ApiResponse;

public class ResponseUtil {
    
    /**
     * 创建成功的响应
     * @param data 数据
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }
    
    /**
     * 创建带消息的成功响应
     * @param data 数据
     * @param message 消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.success(data, message);
    }
    
    /**
     * 创建错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.error(message);
    }
    
    /**
     * 创建带数据的错误响应
     * @param message 错误消息
     * @param data 数据
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.error(message, data);
    }
}