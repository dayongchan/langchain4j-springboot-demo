package com.angelai.assistant.demo.langchain4j.service;

import com.angelai.assistant.demo.langchain4j.service.llm.SearchEnhancedAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LlmServiceTest {

    @Autowired
    private LlmService llmService;

    @Test
    void testCreateSearchEnhancedAssistant() {
        // When
        SearchEnhancedAssistant assistant = llmService.createSearchEnhancedAssistant();
        
        // Then
        assertNotNull(assistant, "SearchEnhancedAssistant should not be null");
    }
}