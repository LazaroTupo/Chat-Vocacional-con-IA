package com.vocacional.repository;

import com.vocacional.model.Career;
import org.springframework.data.mongodb.repository.Aggregation;
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

    @Aggregation(pipeline = {
            "{ $match: { $or: [ " +
                    "  { 'nombre': { $regex: ?0, $options: 'i' } }, " +
                    "  { 'nombre': { $regex: ?1, $options: 'i' } }, " +
                    "  { 'keywords': { $in: ?2 } } " +
                    "] } }",
            "{ $addFields: { " +
                    "  matchScore: { " +
                    "    $switch: { " +
                    "      branches: [ " +
                    "        { case: { $eq: ['$nombre', ?0] }, then: 100 }, " +
                    "        { case: { $regexMatch: { input: '$nombre', regex: ?0, options: 'i' } }, then: 90 }, " +
                    "        { case: { $regexMatch: { input: '$nombre', regex: ?1, options: 'i' } }, then: 80 }, " +
                    "        { case: { $gt: [{ $size: { $setIntersection: ['$keywords', ?2] } }, 0] }, then: 70 } " +
                    "      ], " +
                    "      default: 60 " +
                    "    } " +
                    "  } " +
                    "} }",
            "{ $sort: { matchScore: -1 } }",
            "{ $limit: 1 }"
    })
    Optional<Career> findBestCareerMatch(String fullName, String mainKeyword, List<String> searchKeywords);
}
