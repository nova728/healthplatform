// healthplatform/src/main/java/com/health/healthplatform/service/NotificationService.java

package com.health.healthplatform.service;

import org.springframework.stereotype.Service;

import com.health.healthplatform.websocket.NotificationWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {
    
    public void sendNotification(String userId, String message) {
        try {
            NotificationWebSocketHandler.sendNotification(userId, message);
            log.info("发送通知给用户 {}: {}", userId, message);
        } catch (Exception e) {
            log.error("发送通知失败: {}", e.getMessage());
        }
    }
}