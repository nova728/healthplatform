package com.health.healthplatform.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable String userId) {
        // 实现获取通知的逻辑
        return ResponseEntity.ok(Collections.emptyList());  // 临时返回空列表
    }
}
