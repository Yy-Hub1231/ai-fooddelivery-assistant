//全局异常捕获

package com.student.fooddelivery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice：全局捕获所有@RestController抛出的异常
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(Exception.class) 捕获所有Exception类型异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        String message = e.getMessage();

        // 判断异常信息包含401 Unauthorized：阿里云API Key错误
        if (message != null && message.contains("401 Unauthorized")) {
            result.put("message", "API Key 无效，请检查 ALI_API_KEY 配置是否正确");
            result.put("code", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        // 判断数据库连接异常
        if (message != null && message.contains("Unknown database") || message.contains("Access denied")) {
            result.put("message", "数据库连接失败，请检查数据库配置");
            result.put("code", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        // 默认其他异常
        result.put("message", "服务异常：" + e.getMessage());
        result.put("code", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
