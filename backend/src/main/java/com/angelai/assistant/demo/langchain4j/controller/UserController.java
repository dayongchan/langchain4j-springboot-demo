package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.dto.ApiResponse;
import com.angelai.assistant.demo.langchain4j.dto.LoginRequest;
import com.angelai.assistant.demo.langchain4j.dto.RegisterRequest;
import com.angelai.assistant.demo.langchain4j.dto.UserDto;
import com.angelai.assistant.demo.langchain4j.entity.Conversation;
import com.angelai.assistant.demo.langchain4j.entity.Message;
import com.angelai.assistant.demo.langchain4j.service.ConversationService;
import com.angelai.assistant.demo.langchain4j.service.UserService;
import com.angelai.assistant.demo.langchain4j.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            UserDto userDto = userService.register(registerRequest);
            return ResponseEntity.ok(ResponseUtil.success(userDto, "注册成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDto userDto = userService.login(loginRequest);
            return ResponseEntity.ok(ResponseUtil.success(userDto, "登录成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}/conversations")
    public ResponseEntity<ApiResponse<List<Conversation>>> getUserConversations(@PathVariable Long userId) {
        try {
            List<Conversation> conversations = conversationService.getUserConversations(userId);
            return ResponseEntity.ok(ResponseUtil.success(conversations, "获取对话列表成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }

    @PostMapping("/{userId}/conversations")
    public ResponseEntity<ApiResponse<Conversation>> createConversation(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            Conversation conversation = conversationService.createConversation(userId, title);
            return ResponseEntity.ok(ResponseUtil.success(conversation, "创建对话成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }

    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<ApiResponse<String>> deleteConversation(@PathVariable Long conversationId) {
        try {
            conversationService.deleteConversation(conversationId);
            return ResponseEntity.ok(ResponseUtil.success(null, "对话删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ApiResponse<List<Message>>> getConversationMessages(@PathVariable Long conversationId) {
        try {
            List<Message> messages = conversationService.getConversationMessages(conversationId);
            return ResponseEntity.ok(ResponseUtil.success(messages, "获取消息成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ApiResponse<Message>> saveMessage(@PathVariable Long conversationId, @RequestBody Message message) {
        try {
            Message savedMessage = conversationService.saveMessage(conversationId, message);
            return ResponseEntity.ok(ResponseUtil.success(savedMessage, "保存消息成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error(e.getMessage()));
        }
    }
}