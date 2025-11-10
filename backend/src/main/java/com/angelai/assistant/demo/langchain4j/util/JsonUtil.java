package com.angelai.assistant.demo.langchain4j.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 将JSON字符串转换为指定类型的对象
     * @param json JSON字符串
     * @param clazz 目标类
     * @param <T> 泛型类型
     * @return 转换后的对象
     * @throws JsonProcessingException JSON处理异常
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
    
    /**
     * 将对象转换为JSON字符串
     * @param object 对象
     * @return JSON字符串
     * @throws JsonProcessingException JSON处理异常
     */
    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}