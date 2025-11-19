package com.vocacional.repository;

import com.vocacional.model.University;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends MongoRepository<University, String> {

    // Buscar por nombre exacto
    Optional<University> findByNombre(String nombre);

    // Buscar por similitud en el nombre (case insensitive)
    List<University> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por país
    List<University> findByPais(String pais);

    // Buscar por similitud en nombre y país
    List<University> findByNombreContainingIgnoreCaseAndPais(String nombre, String pais);

    // Buscar todas las universidades ordenadas por nombre
    List<University> findAllByOrderByNombreAsc();
}
