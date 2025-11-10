package com.vocacional.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenRouterResponse {
    private List<Choice> choices;
    private Usage usage;

    @Data
    public static class Choice {
        private Message message;
    }
    @Data
    public static class Message {
        private String content;
    }

    @Data
    public static class Usage {
        private Integer total_tokens;
    }
}
