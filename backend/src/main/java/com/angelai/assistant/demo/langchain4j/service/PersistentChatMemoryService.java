package com.angelai.assistant.demo.langchain4j.service;

import com.angelai.assistant.demo.langchain4j.entity.Message;
import com.angelai.assistant.demo.langchain4j.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持久化聊天内存服务
 */
@Service
public class PersistentChatMemoryService implements ChatMemoryStore {
    
    @Autowired
    private MessageRepository messageRepository;
    
    // 内存缓存，提高访问效率
    private final Map<Object, List<ChatMessage>> cache = new ConcurrentHashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        try {
            // memoryId 实际上是 conversationId
            // 如果是默认ID，则返回空列表
            if ("default".equals(memoryId.toString())) {
                return new ArrayList<>();
            }
            
            Long conversationId = Long.valueOf(memoryId.toString());
            
            // 从数据库获取消息
            List<Message> dbMessages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
            
            // 转换为 ChatMessage 列表
            List<ChatMessage> chatMessages = new ArrayList<>();
            for (Message dbMessage : dbMessages) {
                // 直接使用内容创建消息对象而不是尝试JSON解析
                if ("USER".equals(dbMessage.getSenderType())) {
                    chatMessages.add(new UserMessage(dbMessage.getContent()));
                } else if ("AI".equals(dbMessage.getSenderType())) {
                    chatMessages.add(new AiMessage(dbMessage.getContent()));
                }
                // PROCESSING类型的消息不添加到历史中
            }
            
            return chatMessages;
        } catch (Exception e) {
            // 发生异常时返回空列表
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // 作为内存存储，我们只需要更新缓存，不需要持久化到数据库
        // 消息的持久化由前端通过API调用完成
        // 这里只更新内存缓存
        cache.put(memoryId, new ArrayList<>(messages));
    }

    @Override
    public void deleteMessages(Object memoryId) {
        try {
            // memoryId 实际上是 conversationId
            // 如果是默认ID，则不进行数据库操作
            if ("default".equals(memoryId.toString())) {
                return;
            }
            
            Long conversationId = Long.valueOf(memoryId.toString());
            
            // 删除与对话相关联的所有消息
            List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
            messageRepository.deleteAll(messages);
        } catch (Exception e) {
            // 记录错误但不中断流程
            e.printStackTrace();
        }
    }
}
