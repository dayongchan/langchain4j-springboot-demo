package com.angelai.assistant.demo.langchain4j.config;

import com.angelai.assistant.demo.langchain4j.event.DefaultChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Langchain4jConfiguration {
    @Bean
    ChatModelListener chatModelListener() {
        return new DefaultChatModelListener();
    }
}
