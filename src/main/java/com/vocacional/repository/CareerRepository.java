package com.vocacional.repository;

import com.vocacional.model.Career;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends MongoRepository<Career, String> {

    // Buscar por nombre exacto
    Optional<Career> findByNombre(String nombre);

    // Buscar por similitud en el nombre (case insensitive)
    List<Career> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por keywords
    List<Career> findByKeywordsContaining(String keyword);

    // Buscar todas las carreras ordenadas por nombre
    List<Career> findAllByOrderByNombreAsc();
}
