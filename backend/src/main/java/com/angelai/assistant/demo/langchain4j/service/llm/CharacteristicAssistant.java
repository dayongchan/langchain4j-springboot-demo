package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 描述特性的助手
 */
public interface CharacteristicAssistant {
    @SystemMessage("You are a {{characteristic}} assistant")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage, @V("characteristic") String characteristic);
}
