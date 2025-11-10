package com.angelai.assistant.demo.langchain4j.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 持久化聊天内存服务
 */
@Service
public class PersistentChatMemoryService implements ChatMemoryStore {
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // TODO: 实现通过内存ID从持久化存储中获取所有消息。
        // 可以使用ChatMessageDeserializer.messageFromJson(String)和
        // ChatMessageDeserializer.messagesFromJson(String)辅助方法
        // 轻松地从JSON反序列化聊天消息。
        return null;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // TODO: 实现通过内存ID更新持久化存储中的所有消息。
        // 可以使用ChatMessageSerializer.messageToJson(ChatMessage)和
        // ChatMessageSerializer.messagesToJson(List<ChatMessage>)辅助方法
        // 轻松地将聊天消息序列化为JSON。
    }

    @Override
    public void deleteMessages(Object memoryId) {
        // TODO: 实现通过内存ID删除持久化存储中的所有消息。
    }
}
