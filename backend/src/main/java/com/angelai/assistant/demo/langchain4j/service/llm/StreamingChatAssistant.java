package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface StreamingChatAssistant {

    @SystemMessage("You are a helpful assistant. Answer in Chinese.")
    Flux<String> chat(@UserMessage String userMessage);
}
