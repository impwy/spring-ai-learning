package com.backai.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backai.ai.service.ChatService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai/chat")
public class AiController {
    private final ChatService chatService;

    public AiController(ChatService chatService) {this.chatService = chatService;}

    @GetMapping
    public Flux<String> streamChat(String message) {
        return chatService.streamChatResponse(message);
    }
}
