package com.vocacional.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "carreras")
@Data
public class Career {
    @Id
    private String id;

    private String nombre;
    private String imagenUrl;
    private String contentType;
    private List<String> keywords;
    private LocalDateTime fechaCreacion;

    // Constructores
    public Career() {}

    public Career(String nombre, String imagenUrl, List<String> keywords) {
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.keywords = keywords;
        this.fechaCreacion = LocalDateTime.now();
    }

}
