package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.service.StreamingChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StreamingChatAssistant streamingChatAssistant;

    @Test
    void streaming_shouldReturnFluxString() throws Exception {
        // Given
        String inputMessage = "Hello";
        Flux<String> expectedResponse = Flux.just("Hello", "World");

        when(streamingChatAssistant.chat(anyString()))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/chat/streaming")
                .contentType(MediaType.TEXT_PLAIN)
                .content(inputMessage))
                .andExpect(status().isOk());
    }
}