package com.health.healthplatform.controller;

import com.health.healthplatform.service.ChatService;
import com.health.healthplatform.DTO.ChatRequest;
import com.health.healthplatform.DTO.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        ChatResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(response);
    }
}