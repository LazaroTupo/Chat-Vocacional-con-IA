package com.vocacional.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatResponse {
    private String response;
    private String sessionId;
    private String modelUsed;
    private LocalDateTime timestamp;
    private int totalMessages;

    public ChatResponse(String response, String sessionId, String modelUsed, int totalMessages) {
        this.response = response;
        this.sessionId = sessionId;
        this.modelUsed = modelUsed;
        this.timestamp = LocalDateTime.now();
        this.totalMessages = totalMessages;
    }
}
