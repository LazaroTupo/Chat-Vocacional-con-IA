package com.vocacional.service;

import com.vocacional.model.Career;
import com.vocacional.model.University;
import com.vocacional.repository.CareerRepository;
import com.vocacional.repository.UniversityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageSearchService {

    private final CareerRepository careerRepository;

    private final UniversityRepository universityRepository;

    /**
     * Busca la imagen de una carrera por nombre o similitud
     */
    public ImageSearchService(CareerRepository careerRepository, UniversityRepository universityRepository) {
        this.careerRepository = careerRepository;
        this.universityRepository = universityRepository;
    }

    public String findCareerImage(String careerName) {
        if (careerName == null || careerName.trim().isEmpty()) {
            return getDefaultCareerImage();
        }

        // 1. Buscar por nombre exacto
        Optional<Career> exactMatch = careerRepository.findByNombre(careerName.trim());
        if (exactMatch.isPresent() && exactMatch.get().getImagenUrl() != null) {
            return exactMatch.get().getImagenUrl();
        }

        // 2. Buscar por similitud en el nombre
        List<Career> similarCareers = careerRepository.findByNombreContainingIgnoreCase(careerName.trim());
        for (Career career : similarCareers) {
            if (career.getImagenUrl() != null && !career.getImagenUrl().isEmpty()) {
                return career.getImagenUrl();
            }
        }

        // 3. Buscar por keywords
        String[] words = careerName.toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.length() > 3) { // Solo palabras de más de 3 caracteres
                List<Career> keywordMatches = careerRepository.findByKeywordsContaining(word);
                for (Career career : keywordMatches) {
                    if (career.getImagenUrl() != null && !career.getImagenUrl().isEmpty()) {
                        return career.getImagenUrl();
                    }
                }
            }
        }

        return getDefaultCareerImage();
    }

    /**
     * Busca la imagen de una universidad por nombre o similitud
     */
    public String findUniversityImage(String universityName) {
        if (universityName == null || universityName.trim().isEmpty()) {
            return getDefaultUniversityImage();
        }

        // 1. Buscar por nombre exacto
        Optional<University> exactMatch = universityRepository.findByNombre(universityName.trim());
        if (exactMatch.isPresent() && exactMatch.get().getImagenUrl() != null) {
            return exactMatch.get().getImagenUrl();
        }

        // 2. Buscar por similitud en el nombre
        List<University> similarUniversities = universityRepository.findByNombreContainingIgnoreCase(universityName.trim());
        for (University university : similarUniversities) {
            if (university.getImagenUrl() != null && !university.getImagenUrl().isEmpty()) {
                return university.getImagenUrl();
            }
        }

        return getDefaultUniversityImage();
    }

    /**
     * Busca información completa de una carrera
     */
    public Optional<Career> findCareerInfo(String careerName) {
        if (careerName == null || careerName.trim().isEmpty()) {
            return Optional.empty();
        }

        // Buscar por nombre exacto primero
        Optional<Career> exactMatch = careerRepository.findByNombre(careerName.trim());
        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // Buscar por similitud
        List<Career> similarCareers = careerRepository.findByNombreContainingIgnoreCase(careerName.trim());
        return similarCareers.stream().findFirst();
    }

    /**
     * Busca información completa de una universidad
     */
    public Optional<University> findUniversityInfo(String universityName) {
        if (universityName == null || universityName.trim().isEmpty()) {
            return Optional.empty();
        }

        // Buscar por nombre exacto primero
        Optional<University> exactMatch = universityRepository.findByNombre(universityName.trim());
        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // Buscar por similitud
        List<University> similarUniversities = universityRepository.findByNombreContainingIgnoreCase(universityName.trim());
        return similarUniversities.stream().findFirst();
    }

    private String getDefaultCareerImage() {
        return "https://images.pexels.com/photos/356030/pexels-photo-356030.jpeg";
    }

    private String getDefaultUniversityImage() {
        return "https://images.pexels.com/photos/267885/pexels-photo-267885.jpeg";
    }
}