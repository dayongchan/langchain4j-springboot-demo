package com.angelai.assistant.demo.langchain4j.exception;

import com.angelai.assistant.demo.langchain4j.dto.ApiResponse;
import com.angelai.assistant.demo.langchain4j.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理访问拒绝异常（权限不足）
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponse<String> errorResponse = ResponseUtil.error("访问被拒绝: " + ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * 处理认证失败异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ApiResponse<String> errorResponse = ResponseUtil.error("认证失败: " + ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理认证不足异常
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, WebRequest request) {
        ApiResponse<String> errorResponse = ResponseUtil.error("认证信息不足: " + ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNoHandlerFound(NoHandlerFoundException ex) {
        ApiResponse<String> errorResponse = ResponseUtil.error("请求的资源不存在: " + ex.getRequestURL());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理所有其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex, WebRequest request) {
        ApiResponse<String> errorResponse = ResponseUtil.error("服务器内部错误: " + ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        // 记录错误日志
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiResponse<String> errorResponse = ResponseUtil.error("运行时错误: " + ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}