package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.enums.Sentiment;
import com.angelai.assistant.demo.langchain4j.service.GeoLocationService;
import com.angelai.assistant.demo.langchain4j.service.LlmService;
import com.angelai.assistant.demo.langchain4j.service.PromptTemplateService;
import com.angelai.assistant.demo.langchain4j.service.llm.EnhancedAssistant;
import com.angelai.assistant.demo.langchain4j.service.llm.SentimentAnalyzer;
import com.angelai.assistant.demo.langchain4j.service.llm.StreamingChatAssistant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private GeoLocationService geoLocationService;

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Autowired
    private LlmService llmService;

    @GetMapping("/message")
    public String message(@RequestParam String msg, HttpServletRequest request,
                          @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                          @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) {

        // 获取用户真实IP地址
        String userIp = geoLocationService.getRealIpAddress(request.getRemoteAddr(), xForwardedFor, xRealIp);

        //判断用户当前语言的情感
        SentimentAnalyzer sentimentAnalyzer = llmService.createSentimentAnalyzer();
        Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf(msg);

        // 构建系统提示词
        String systemPrompt = promptTemplateService.buildSystemPrompt(userIp, msg,  sentiment);

        // 使用增强版助手，可以动态传入系统提示词
        EnhancedAssistant assistant = llmService.createEnhancedAssistant();
        String answer = assistant.chat(systemPrompt);
        System.out.println(answer);
        return answer;
    }

    @PostMapping("/streaming")
    public Flux<String> streaming(@RequestBody String msg, HttpServletRequest request,
                                  @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                  @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) {
        // 获取用户真实IP地址
        String userIp = geoLocationService.getRealIpAddress(request.getRemoteAddr(), xForwardedFor, xRealIp);

        //判断用户当前语言的情感
        SentimentAnalyzer sentimentAnalyzer = llmService.createSentimentAnalyzer();
        Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf(msg);

        // 构建系统提示词
        String systemPrompt = promptTemplateService.buildSystemPrompt(userIp, msg, sentiment);

        // 使用增强版流式助手
        StreamingChatAssistant assistant = llmService.createStreamingEnhancedAssistant();
        return assistant.chat(systemPrompt);
    }
}