package com.angelai.assistant.demo.langchain4j.repository;

import com.angelai.assistant.demo.langchain4j.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    List<Message> findByUserIdOrderByCreatedAtAsc(Long userId);
}