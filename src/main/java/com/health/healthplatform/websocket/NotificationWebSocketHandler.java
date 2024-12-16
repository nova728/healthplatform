// healthplatform/src/main/java/com/health/healthplatform/websocket/NotificationWebSocketHandler.java

package com.health.healthplatform.websocket;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    
    // 存储用户ID和WebSocket会话的映射
    private static final ConcurrentHashMap<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = extractUserId(session);
        SESSIONS.put(userId, session);
        log.info("用户 {} 建立WebSocket连接", userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userId = extractUserId(session);
        log.info("收到用户 {} 的消息: {}", userId, message.getPayload());
        // 这里可以处理接收到的消息
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = extractUserId(session);
        SESSIONS.remove(userId);
        log.info("用户 {} 断开WebSocket连接", userId);
    }

    // 提取用户ID从session
    private String extractUserId(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    // 发送通知给指定用户
    public static void sendNotification(String userId, String message) {
        WebSocketSession session = SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("发送通知失败: {}", e.getMessage());
            }
        }
    }
}