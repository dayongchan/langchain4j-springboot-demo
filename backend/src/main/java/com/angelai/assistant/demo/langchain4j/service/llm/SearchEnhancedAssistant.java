package com.angelai.assistant.demo.langchain4j.service.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SearchEnhancedAssistant {
    
    @SystemMessage("You are a helpful assistant. Use the provided search results to answer the user's question. If the search results don't contain the answer, say so.")
    String chatWithSearchResults(@UserMessage String userMessageAndSearchResults);
}