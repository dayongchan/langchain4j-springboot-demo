package com.angelai.assistant.demo.langchain4j.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService
//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "openAiStreamingChatModel")
public interface StreamingChatAssistant {
    @SystemMessage("You are a polite assistant")
    Flux<String> chat(String userMessage);
}
