package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.service.StreamingChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import com.angelai.assistant.demo.langchain4j.service.OpenAiAssistant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StreamingChatAssistant streamingChatAssistant;
    /**
     * 模拟 OpenAiAssistant bean，避免真实调用 AI 服务
     */
    @MockBean
    private OpenAiAssistant openAiAssistant;

    /**
     * 测试 /api/chat/message 接口是否能正确返回 chat 响应
     *
     * @throws Exception 如果执行过程中发生异常
     */
    @Test
    void message_shouldReturnCorrectResponse() throws Exception {
        // Given: 给定输入和期望输出
        String inputMsg = "你好";
        String expectedOutput = "你好，世界！";

        // 当调用 openAiAssistant.chat(anyString()) 时，返回预设的结果
        when(openAiAssistant.chat(anyString())).thenReturn(expectedOutput);

        // When & Then: 发起 GET 请求并验证状态码及返回内容
        mockMvc.perform(get("/api/chat/message")
                        .param("msg", inputMsg)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

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