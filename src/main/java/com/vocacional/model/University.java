package com.vocacional.model;

import lombok.Data;

@Data
public class University {
    private String name;
    private String location;
    private String careerOffered;
    private String proximity;

    // Constructores, Getters y Setters
    public University() {}

    public University(String name, String location, String careerOffered, String proximity) {
        this.name = name;
        this.location = location;
        this.careerOffered = careerOffered;
        this.proximity = proximity;
    }
}
