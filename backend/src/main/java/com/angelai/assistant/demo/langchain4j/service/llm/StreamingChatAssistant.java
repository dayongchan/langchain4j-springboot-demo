package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

@SystemMessage("你是一个智能AI助手，请用中文回答用户问题。")
public interface StreamingChatAssistant {
    Flux<String> chat(@UserMessage String userMessage);
}
