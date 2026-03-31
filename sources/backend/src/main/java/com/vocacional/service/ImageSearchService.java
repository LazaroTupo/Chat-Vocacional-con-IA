package com.vocacional.service;

import com.vocacional.model.Career;
import com.vocacional.model.University;
import com.vocacional.repository.CareerRepository;
import com.vocacional.repository.UniversityRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageSearchService {

    private final CareerRepository careerRepository;
    private final UniversityRepository universityRepository;

    private static final String DEFAULT_CAREER_IMAGE = "https://images.pexels.com/photos/356030/pexels-photo-356030.jpeg";
    private static final String DEFAULT_UNIVERSITY_IMAGE = "https://images.pexels.com/photos/267885/pexels-photo-267885.jpeg";

    public ImageSearchService(CareerRepository careerRepository, UniversityRepository universityRepository) {
        this.careerRepository = careerRepository;
        this.universityRepository = universityRepository;
    }

    public Optional<Career> findCareerInfo(String careerName) {
        if (careerName == null || careerName.trim().isEmpty()) {
            return Optional.empty();
        }

        String cleanName = normalizeSearchText(careerName);
        String mainKeyword = extractMainKeyword(cleanName);
        List<String> searchKeywords = extractSearchKeywords(cleanName);

        return careerRepository.findBestCareerMatch(cleanName, mainKeyword, searchKeywords);
    }

    public Optional<University> findUniversityInfo(String universityName) {
        if (universityName == null || universityName.trim().isEmpty()) {
            return Optional.empty();
        }

        String cleanName = normalizeSearchText(universityName);
        String mainKeyword = extractMainKeyword(cleanName);
        List<String> searchKeywords = extractSearchKeywords(cleanName);

        return universityRepository.findBestUniversityMatch(cleanName, mainKeyword, searchKeywords);
    }

    private List<String> extractSearchKeywords(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }

        String[] stopwords = {"de", "del", "la", "el", "los", "las", "y", "en", "a", "para"};

        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> word.length() > 3)
                .filter(word -> Arrays.stream(stopwords).noneMatch(stop -> stop.equals(word)))
                .collect(Collectors.toList());
    }

    private String normalizeSearchText(String text) {
        if (text == null) {
            return "";
        }

        // Eliminar contenido entre paréntesis (ej: "(UNMSM)")
        String normalized = text.replaceAll("\\([^)]*\\)", "");

        // Eliminar guiones y otros símbolos comunes
        normalized = normalized.replaceAll("[-_]", " ");

        // Eliminar múltiples espacios y trim
        normalized = normalized.replaceAll("\\s+", " ").trim();

        return normalized;
    }

    private String extractMainKeyword(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        // Palabras a ignorar (stopwords comunes en español)
        String[] stopwords = {"de", "del", "la", "el", "los", "las", "y", "en", "a", "para"};

        String[] words = text.toLowerCase().split("\\s+");
        String longestWord = "";

        for (String word : words) {
            // Ignorar stopwords y palabras cortas
            boolean isStopword = false;
            for (String stopword : stopwords) {
                if (word.equals(stopword)) {
                    isStopword = true;
                    break;
                }
            }

            if (!isStopword && word.length() > longestWord.length() && word.length() > 3) {
                longestWord = word;
            }
        }

        return longestWord;
    }

    public String getCareerImageOrDefault(Optional<Career> career) {
        return career
                .map(Career::getImagenUrl)
                .filter(url -> url != null && !url.isEmpty())
                .orElse(DEFAULT_CAREER_IMAGE);
    }

    public String getUniversityImageOrDefault(Optional<University> university) {
        return university
                .map(University::getImagenUrl)
                .filter(url -> url != null && !url.isEmpty())
                .orElse(DEFAULT_UNIVERSITY_IMAGE);
    }

    public List<String> getCareerKeywordsOrEmpty(Optional<Career> career) {
        return career
                .map(Career::getKeywords)
                .orElse(List.of());
    }

    public String getUniversityCountryOrDefault(Optional<University> university) {
        return university
                .map(University::getPais)
                .orElse("País no especificado");
    }
}