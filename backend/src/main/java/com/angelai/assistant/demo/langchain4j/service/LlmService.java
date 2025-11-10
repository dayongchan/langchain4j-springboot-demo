package com.angelai.assistant.demo.langchain4j.service;

import com.angelai.assistant.demo.langchain4j.enums.ChatType;
import com.angelai.assistant.demo.langchain4j.service.llm.Assistant;
import com.angelai.assistant.demo.langchain4j.service.llm.EnhancedAssistant;
import com.angelai.assistant.demo.langchain4j.service.llm.SentimentAnalyzer;
import com.angelai.assistant.demo.langchain4j.service.llm.StreamingChatAssistant;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LlmService {
    @Value("${langchain4j.open-ai.streaming-chat-model.api-key}")
    private String apiKey;
    @Value("${langchain4j.open-ai.streaming-chat-model.base-url}")
    private String baseUrl;
    @Value("${langchain4j.open-ai.streaming-chat-model.model-name}")
    private String modelName;
    @Autowired
    private PersistentChatMemoryService persistentChatMemoryService;

    public Assistant createAssistant(ChatType chatType) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryService)
                .build();
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();

        /*switch (chatType){
            case CHAT -> {
                return AiServices.create(Assistant.class, model);
            }
            case WEB_SEARCH -> {
                return AiServices.create(Assistant.class, model);
            }
        }*/
        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
    
    public EnhancedAssistant createEnhancedAssistant() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryService)
                .build();
                
        return AiServices.create(EnhancedAssistant.class, model);
    }
    
    public StreamingChatAssistant createStreamingEnhancedAssistant() {
        StreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();
                
        return AiServices.create(StreamingChatAssistant.class, model);
    }

    public SentimentAnalyzer createSentimentAnalyzer() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();

        return AiServices.create(SentimentAnalyzer.class, model);
    }
}