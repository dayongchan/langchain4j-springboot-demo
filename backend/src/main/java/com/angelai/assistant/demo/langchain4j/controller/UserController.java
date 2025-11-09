package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.dto.LoginRequest;
import com.angelai.assistant.demo.langchain4j.dto.RegisterRequest;
import com.angelai.assistant.demo.langchain4j.dto.UserDto;
import com.angelai.assistant.demo.langchain4j.entity.Conversation;
import com.angelai.assistant.demo.langchain4j.entity.Message;
import com.angelai.assistant.demo.langchain4j.service.UserService;
import com.angelai.assistant.demo.langchain4j.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ConversationService conversationService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            UserDto userDto = userService.register(registerRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", userDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDto userDto = userService.login(loginRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", userDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{userId}/conversations")
    public ResponseEntity<?> getUserConversations(@PathVariable Long userId) {
        try {
            List<Conversation> conversations = conversationService.getUserConversations(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("conversations", conversations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/{userId}/conversations")
    public ResponseEntity<?> createConversation(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            Conversation conversation = conversationService.createConversation(userId, title);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("conversation", conversation);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable Long conversationId) {
        try {
            conversationService.deleteConversation(conversationId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "对话删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<?> getConversationMessages(@PathVariable Long conversationId) {
        try {
            List<Message> messages = conversationService.getConversationMessages(conversationId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("messages", messages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<?> saveMessage(@PathVariable Long conversationId, @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String content = (String) request.get("content");
            String senderType = (String) request.get("senderType");
            
            Message message = conversationService.saveMessage(conversationId, userId, content, senderType);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}