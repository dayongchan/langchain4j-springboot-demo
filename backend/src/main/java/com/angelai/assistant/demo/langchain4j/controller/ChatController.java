package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.dto.ApiResponse;
import com.angelai.assistant.demo.langchain4j.enums.Sentiment;
import com.angelai.assistant.demo.langchain4j.service.GeoLocationService;
import com.angelai.assistant.demo.langchain4j.service.LlmService;
import com.angelai.assistant.demo.langchain4j.service.PromptTemplateService;
import com.angelai.assistant.demo.langchain4j.service.SearchService;
import com.angelai.assistant.demo.langchain4j.service.llm.EnhancedAssistant;
import com.angelai.assistant.demo.langchain4j.service.llm.SearchEnhancedAssistant;
import com.angelai.assistant.demo.langchain4j.service.llm.SentimentAnalyzer;
import com.angelai.assistant.demo.langchain4j.service.llm.StreamingChatAssistant;
import com.angelai.assistant.demo.langchain4j.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private GeoLocationService geoLocationService;

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Autowired
    private LlmService llmService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/message")
    public ResponseEntity<ApiResponse<String>> message(@RequestParam String msg, HttpServletRequest request,
                                                       @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                                       @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) {
        try {
            // 获取用户真实IP地址
            String userIp = geoLocationService.getRealIpAddress(request.getRemoteAddr(), xForwardedFor, xRealIp);

            //判断用户当前语言的情感
            SentimentAnalyzer sentimentAnalyzer = llmService.createSentimentAnalyzer();
            Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf(msg);

            // 构建系统提示词
            String systemPrompt = promptTemplateService.buildSystemPrompt(userIp, msg, sentiment);

            // 使用增强版助手，可以动态传入系统提示词
            EnhancedAssistant assistant = llmService.createEnhancedAssistant();
            String answer = assistant.chat(systemPrompt);
            return ResponseEntity.ok(ResponseUtil.success(answer, "请求成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("处理消息时出错: " + e.getMessage()));
        }
    }

    @GetMapping("/search-message")
    public ResponseEntity<ApiResponse<String>> searchMessage(@RequestParam String msg, HttpServletRequest request,
                                                             @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                                             @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) {
        try {
            // 获取用户真实IP地址
            String userIp = geoLocationService.getRealIpAddress(request.getRemoteAddr(), xForwardedFor, xRealIp);

            //判断用户当前语言的情感
            SentimentAnalyzer sentimentAnalyzer = llmService.createSentimentAnalyzer();
            Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf(msg);

            // 构建系统提示词
            String systemPrompt = promptTemplateService.buildSystemPrompt(userIp, msg, sentiment);

            // 执行网络搜索
            String searchResults = searchService.formatSearchResults(searchService.search(msg));

            // 将搜索结果与用户消息结合
            String combinedPrompt = systemPrompt + "\n\n" + searchResults;

            // 使用搜索增强版助手
            SearchEnhancedAssistant assistant = llmService.createSearchEnhancedAssistant();
            String answer = assistant.chatWithSearchResults(combinedPrompt);
            return ResponseEntity.ok(ResponseUtil.success(answer, "请求成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("处理消息时出错: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/streaming", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> streaming(HttpServletRequest request,
                          @RequestBody String msg,
                          @RequestParam(required = false) String conversationId,
                          @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                          @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) throws Exception {
        // 获取用户真实IP地址
        String userIp = geoLocationService.getRealIpAddress(request.getRemoteAddr(), xForwardedFor, xRealIp);

        //判断用户当前语言的情感
        SentimentAnalyzer sentimentAnalyzer = llmService.createSentimentAnalyzer();
        Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf(msg);

        // 构建系统提示词
        String systemPrompt = promptTemplateService.buildSystemPrompt(userIp, msg, sentiment);

        // 转换对话ID
        Long convId = null;
        if (conversationId != null && !conversationId.isEmpty()) {
            try {
                convId = Long.valueOf(conversationId);
            } catch (NumberFormatException e) {
                // 如果转换失败，使用null
            }
        }

        // 使用增强版流式助手
        StreamingChatAssistant assistant = llmService.createStreamingEnhancedAssistant(convId);

        // 处理流式响应
        return assistant.chat(systemPrompt)
                .doOnError(error -> System.err.println("Stream error: " + error.getMessage()))
                .doOnComplete(() -> System.out.println("Stream completed successfully"));
    }

    @PostMapping(value = "/streaming-search", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> streamingSearch(@RequestBody String msg,
                                @RequestParam(required = false) String conversationId,
                                HttpServletRequest request,
                                @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
                                @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) throws Exception {
        // 获取用户真实IP地址
        String userIp = geoLocationService.getRealIpAddress(request.getRemoteAddr(), xForwardedFor, xRealIp);

        //判断用户当前语言的情感
        SentimentAnalyzer sentimentAnalyzer = llmService.createSentimentAnalyzer();
        Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf(msg);

        // 构建系统提示词
        String systemPrompt = promptTemplateService.buildSystemPrompt(userIp, msg, sentiment);

        // 执行网络搜索
        String searchResults = searchService.formatSearchResults(searchService.search(msg));

        // 将搜索结果与用户消息结合
        String combinedPrompt = systemPrompt + "\n\n" + searchResults;

        // 转换对话ID
        Long convId = null;
        if (conversationId != null && !conversationId.isEmpty()) {
            try {
                convId = Long.valueOf(conversationId);
            } catch (NumberFormatException e) {
                // 如果转换失败，使用null
            }
        }

        // 使用增强版流式助手
        StreamingChatAssistant assistant = llmService.createStreamingEnhancedAssistant(convId);

        // 处理流式响应
        return assistant.chat(combinedPrompt)
                .doOnError(error -> System.err.println("Stream search error: " + error.getMessage()))
                .doOnComplete(() -> System.out.println("Stream search completed successfully"));
    }
}