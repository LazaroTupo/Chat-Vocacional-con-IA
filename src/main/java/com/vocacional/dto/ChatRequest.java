package com.vocacional.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String model;

    public ChatRequest(String message) {
        this.message = message;
        this.model = null;
    }
}