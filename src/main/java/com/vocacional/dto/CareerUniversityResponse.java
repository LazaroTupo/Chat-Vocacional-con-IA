package com.vocacional.dto;

import com.vocacional.model.Career;
import com.vocacional.model.University;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CareerUniversityResponse {
    private List<Career> careers;
    private List<University> universities;
    private String sessionId;
    private LocalDateTime generatedAt;

    // Constructores
    public CareerUniversityResponse() {}

    public CareerUniversityResponse(List<Career> careers, List<University> universities, String sessionId) {
        this.careers = careers;
        this.universities = universities;
        this.sessionId = sessionId;
        this.generatedAt = LocalDateTime.now();
    }
}

