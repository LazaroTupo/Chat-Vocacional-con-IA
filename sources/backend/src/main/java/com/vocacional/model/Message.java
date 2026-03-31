package com.vocacional.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class Message {
    private String id; // UUID para mensajes embebidos
    private String content;
    private MessageType type; // USER o SYSTEM
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;

    public Message() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }

    public enum MessageType {
        USER, ASSISTENT, SYSTEM
    }
}