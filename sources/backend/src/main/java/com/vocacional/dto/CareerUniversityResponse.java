package com.vocacional.dto;

import com.vocacional.model.Career;
import com.vocacional.model.University;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CareerUniversityResponse {
    private List<CareerResponse> careers;
    private List<UniversityResponse> universities;
    private String sessionId;
    private LocalDateTime generatedAt;

    // Constructores
    public CareerUniversityResponse() {
        this.generatedAt = LocalDateTime.now();
    }

    public CareerUniversityResponse(List<CareerResponse> careers, List<UniversityResponse> universities, String sessionId) {
        this();
        this.careers = careers;
        this.universities = universities;
        this.sessionId = sessionId;
    }
}