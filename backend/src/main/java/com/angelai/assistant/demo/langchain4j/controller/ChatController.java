package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.service.OpenAiAssistant;
import com.angelai.assistant.demo.langchain4j.service.StreamingChatAssistant;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // 添加CORS支持
public class ChatController {
    @Value("${langchain4j.open-ai.streaming-chat-model.api-key}")
    private String apiKey;
    @Value("${langchain4j.open-ai.streaming-chat-model.base-url}")
    private String baseUrl;
    @Value("${langchain4j.open-ai.streaming-chat-model.model-name}")
    private String modelName;

    @Autowired
    private StreamingChatAssistant streamingChatAssistant;

    @GetMapping("/message")
    public String message(@RequestParam String msg) {
        OpenAiChatModel model = OpenAiChatModel.builder().apiKey(apiKey).baseUrl(baseUrl).modelName(modelName).build();
        String answer = model.chat(msg);
        System.out.println(answer);
        return answer;
    }

    @PostMapping("/streaming")
    public Flux<String> streaming(@RequestBody String msg) {
        return streamingChatAssistant.chat(msg);
    }
}