package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.SystemMessage;

//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "ollamaChatModel")
public interface OllamaAssistant {
    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);
}
