package com.vocacional.dto;

import lombok.Data;

import java.util.List;

@Data
public class CareerResponse {
    private String name;
    private String description;
    private String matchReason;
    private String imageUrl;
    private List<String> keywords;

    // Constructores
    public CareerResponse() {}

    public CareerResponse(String name, String description, String matchReason, String imageUrl) {
        this.name = name;
        this.description = description;
        this.matchReason = matchReason;
        this.imageUrl = imageUrl;
    }

}
