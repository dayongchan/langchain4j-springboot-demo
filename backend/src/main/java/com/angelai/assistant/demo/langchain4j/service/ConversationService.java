package com.angelai.assistant.demo.langchain4j.service;

import com.angelai.assistant.demo.langchain4j.entity.Conversation;
import com.angelai.assistant.demo.langchain4j.entity.Message;
import com.angelai.assistant.demo.langchain4j.repository.ConversationRepository;
import com.angelai.assistant.demo.langchain4j.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {
    
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }
    
    public Conversation createConversation(Long userId, String title) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setTitle(title);
        return conversationRepository.save(conversation);
    }
    
    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }
    
    public Optional<Conversation> getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId);
    }
    
    public List<Message> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
    
    public Message saveMessage(Long conversationId, Long userId, String content, String senderType) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setContent(content);
        message.setSenderType(senderType);
        return messageRepository.save(message);
    }
}