package com.angelai.assistant.demo.langchain4j.service;

import com.angelai.assistant.demo.langchain4j.enums.Sentiment;
import com.angelai.assistant.demo.langchain4j.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提示词模版服务，用于生成系统提示词，后面可以把这个做成保存到数据库的模版，这样就可以动态地更新提示词模版
 *
 * @author Kenyon Chen
 */
@Service
public class PromptTemplateService {

    @Autowired
    private GeoLocationService geoLocationService;

    /**
     * 根据用户的当前的IP地址和输入构建系统提示词模板
     *
     * @param userIp    用户IP地址
     * @param sentiment
     * @return 系统提示词
     */
    public String buildSystemPrompt(String userIp, String userMessage, Sentiment sentiment) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个智能AI助手，请用中文回答用户问题。\n");

        // 添加用户地理位置信息
        String location = geoLocationService.getLocationByIp(userIp);
        prompt.append("用户当前位于: ").append(location).append("\n");

        if (sentiment == Sentiment.NEGATIVE) {
            prompt.append("用户当前情感偏消极，请勿发送负向内容").append("\n");
        }

        // 添加当前时间信息
        String currentTime = TimeUtil.getCurrentTimeInfo();
        prompt.append("当前时间: ").append(currentTime).append("\n");
        // 添加用户名
        if (userMessage != null && !userMessage.isEmpty()) {
            prompt.append("用户的问题是: ").append(userMessage).append("\n");
        }

        // 添加其他上下文信息
        prompt.append("请根据用户的问题提供准确、有帮助的回答。如果用户询问与时间和地点相关的问题，请结合上述信息进行回答。");

        return prompt.toString();
    }
}