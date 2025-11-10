package com.angelai.assistant.demo.langchain4j.util;

import org.springframework.web.client.RestTemplate;

public class HttpUtil {
    
    private static final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 发送HTTP GET请求并返回响应字符串
     * @param url 请求URL
     * @return 响应字符串
     */
    public static String get(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}