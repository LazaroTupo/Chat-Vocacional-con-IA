package com.vocacional.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "universidads")
@Data
public class University {
    @Id
    private String id;

    private String nombre;
    private String imagenUrl;
    private String contentType;
    private String pais;
    private LocalDateTime fechaCreacion;

    // Constructores
    public University() {}

    public University(String nombre, String imagenUrl, String pais) {
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.pais = pais;
        this.fechaCreacion = LocalDateTime.now();
    }

}
