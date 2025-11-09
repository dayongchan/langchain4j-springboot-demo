package com.angelai.assistant.demo.langchain4j.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "openAiChatModel")
public interface OpenAiAssistant {
    @SystemMessage("You are a polite assistant")
    String chat(String userInput);
}
