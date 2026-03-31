package com.vocacional.dto;

import lombok.Data;

@Data
public class UniversityResponse {
    private String name;
    private String location;
    private String careerOffered;
    private String proximity;
    private String imageUrl;
    private String country;

    // Constructores
    public UniversityResponse() {}

    public UniversityResponse(String name, String location, String careerOffered, String proximity, String imageUrl) {
        this.name = name;
        this.location = location;
        this.careerOffered = careerOffered;
        this.proximity = proximity;
        this.imageUrl = imageUrl;
    }
}