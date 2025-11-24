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

    private static final String DEFAULT_CAREER_IMAGE = "https://images.pexels.com/photos/356030/pexels-photo-356030.jpeg";
    private static final String DEFAULT_UNIVERSITY_IMAGE = "https://images.pexels.com/photos/267885/pexels-photo-267885.jpeg";

    public ImageSearchService(CareerRepository careerRepository, UniversityRepository universityRepository) {
        this.careerRepository = careerRepository;
        this.universityRepository = universityRepository;
    }

    /**
     * Busca información completa de una carrera con una sola consulta
     */
    public Optional<Career> findCareerInfo(String careerName) {
        if (careerName == null || careerName.trim().isEmpty()) {
            return Optional.empty();
        }

        String cleanName = normalizeSearchText(careerName);

        // 1. Buscar por nombre exacto con el nombre normalizado
        Optional<Career> exactMatch = careerRepository.findByNombre(cleanName);
        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // 2. Buscar por similitud (más permisivo)
        List<Career> similarCareers = careerRepository.findByNombreContainingIgnoreCase(cleanName);
        if (!similarCareers.isEmpty()) {
            return Optional.of(similarCareers.get(0));
        }

        // 3. Buscar usando palabras clave principales
        String mainKeyword = extractMainKeyword(cleanName);
        if (mainKeyword != null && !mainKeyword.isEmpty()) {
            List<Career> keywordCareers = careerRepository.findByNombreContainingIgnoreCase(mainKeyword);
            if (!keywordCareers.isEmpty()) {
                return Optional.of(keywordCareers.get(0));
            }
        }

        // 4. Buscar por keywords del modelo
        String[] words = cleanName.toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.length() > 3) {
                List<Career> keywordMatches = careerRepository.findByKeywordsContaining(word);
                if (!keywordMatches.isEmpty()) {
                    return Optional.of(keywordMatches.get(0));
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Busca información completa de una universidad con una sola consulta
     */
    public Optional<University> findUniversityInfo(String universityName) {
        if (universityName == null || universityName.trim().isEmpty()) {
            return Optional.empty();
        }

        String cleanName = normalizeSearchText(universityName);

        // 1. Buscar por nombre exacto con el nombre normalizado
        Optional<University> exactMatch = universityRepository.findByNombre(cleanName);
        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // 2. Buscar por similitud (más permisivo)
        List<University> similarUniversities = universityRepository.findByNombreContainingIgnoreCase(cleanName);
        if (!similarUniversities.isEmpty()) {
            return Optional.of(similarUniversities.get(0));
        }

        // 3. Buscar usando palabras clave principales (sin palabras comunes)
        String mainKeyword = extractMainKeyword(cleanName);
        if (mainKeyword != null && !mainKeyword.isEmpty()) {
            List<University> keywordUniversities = universityRepository.findByNombreContainingIgnoreCase(mainKeyword);
            if (!keywordUniversities.isEmpty()) {
                return Optional.of(keywordUniversities.get(0));
            }
        }

        return Optional.empty();
    }

    /**
     * Normaliza el texto de búsqueda eliminando símbolos y contenido entre paréntesis
     */
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

    /**
     * Extrae la palabra clave principal del texto (más larga y significativa)
     */
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

    /**
     * Obtiene la imagen de una carrera o retorna la imagen por defecto
     */
    public String getCareerImageOrDefault(Optional<Career> career) {
        return career
                .map(Career::getImagenUrl)
                .filter(url -> url != null && !url.isEmpty())
                .orElse(DEFAULT_CAREER_IMAGE);
    }

    /**
     * Obtiene la imagen de una universidad o retorna la imagen por defecto
     */
    public String getUniversityImageOrDefault(Optional<University> university) {
        return university
                .map(University::getImagenUrl)
                .filter(url -> url != null && !url.isEmpty())
                .orElse(DEFAULT_UNIVERSITY_IMAGE);
    }

    /**
     * Obtiene las keywords de una carrera o una lista vacía
     */
    public List<String> getCareerKeywordsOrEmpty(Optional<Career> career) {
        return career
                .map(Career::getKeywords)
                .orElse(List.of());
    }

    /**
     * Obtiene el país de una universidad o un texto por defecto
     */
    public String getUniversityCountryOrDefault(Optional<University> university) {
        return university
                .map(University::getPais)
                .orElse("País no especificado");
    }
}