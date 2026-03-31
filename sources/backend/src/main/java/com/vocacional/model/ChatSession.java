package com.vocacional.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "chat_sessions")
@Data
public class ChatSession {
    @Id
    private String id;

    private String userId; // Referencia al usuario
    private String title; // Título automático de la sesión
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Object> metadata;

    // Lista de mensajes embebidos en la sesión
    private List<Message> messages = new ArrayList<>();

    public ChatSession() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
