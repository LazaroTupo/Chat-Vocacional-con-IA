package com.vocacional.model;

import lombok.Data;

@Data
public class Career {
    private String name;
    private String description;
    private String matchReason;

    // Constructores, Getters y Setters
    public Career() {}

    public Career(String name, String description, String matchReason) {
        this.name = name;
        this.description = description;
        this.matchReason = matchReason;
    }
}
