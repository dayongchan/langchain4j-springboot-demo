package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface EnhancedAssistant {
    @SystemMessage("You are a helpful assistant")
    String chat(@UserMessage String userMessage);
}