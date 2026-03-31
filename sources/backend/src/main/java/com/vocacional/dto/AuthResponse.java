package com.vocacional.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthResponse {
    private String message;
    private String username;
    private String userId;
    private boolean success;
    private LocalDateTime timestamp;

    // Constructores
    public AuthResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public AuthResponse(String message, String username, String userId, boolean success) {
        this();
        this.message = message;
        this.username = username;
        this.userId = userId;
        this.success = success;
    }
}
