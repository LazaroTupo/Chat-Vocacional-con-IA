package com.vocacional.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenRouterRequest {
    private String model;
    private List<Message> messages;
    private Integer max_tokens;

    @Data
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Data
    public static class Content {
        private String type;
        private String text;
    }
}