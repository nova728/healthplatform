// healthplatform/src/main/java/com/health/healthplatform/config/WebSocketConfig.java

package com.health.healthplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.health.healthplatform.websocket.NotificationWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler(), "/api/ws/notifications/{userId}")
                .setAllowedOrigins("*");  // 开发环境允许所有源
    }
    
    @Bean
    public WebSocketHandler notificationWebSocketHandler() {
        return new NotificationWebSocketHandler();
    }
}