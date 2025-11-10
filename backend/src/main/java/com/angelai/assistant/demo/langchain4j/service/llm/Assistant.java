package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Assistant {
    @SystemMessage("You are a helpful assistant")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
