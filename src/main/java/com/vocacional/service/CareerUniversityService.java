package com.vocacional.service;

import com.vocacional.dto.CareerResponse;
import com.vocacional.dto.UniversityResponse;
import com.vocacional.dto.CareerUniversityResponse;
import com.vocacional.model.ChatSession;
import com.vocacional.model.Message;
import com.vocacional.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareerUniversityService {

    private final ChatSessionRepository chatSessionRepository;
    private final OpenRouterService openRouterService;
    private final ImageSearchService imageSearchService;

    // Constantes para el prompt
    private static final String CAREER_PROMPT = """
        Hasta ahora ¿Qué carreras son más tentativas para el usuario?, realizar un listado de cada carrera. 
        También en base a la ubicación descrita por el usuario ¿Qué universidades cerca de esa ubicación tienen disponibles esas carreras?, 
        realizar un listado de cada universidad.
        
        FORMATO DE RESPUESTA OBLIGATORIO:
        
        CARRERAS RECOMENDADAS:
        - [Nombre de carrera 1]: [Breve descripción] - [Razón de la recomendación]
        - [Nombre de carrera 2]: [Breve descripción] - [Razón de la recomendación]
        - [Nombre de carrera 3]: [Breve descripción] - [Razón de la recomendación]
        - ...
        
        UNIVERSIDADES RECOMENDADAS:
        - [Nombre universidad 1] - [Ubicación]: Ofrece [carreras ofrecidas] - [Distancia/proximidad]
        - [Nombre universidad 2] - [Ubicación]: Ofrece [carreras ofrecidas] - [Distancia/proximidad]
        - [Nombre universidad 3] - [Ubicación]: Ofrece [carreras ofrecidas] - [Distancia/proximidad]
        - ...
        
        IMPORTANTE: 
        - Solo listar carreras y universidades basadas en la conversación anterior
        - Si no hay suficiente información sobre ubicación, sugerir universidades y carreras genéricas
        - Mantener el formato exacto especificado
        - Usar nombres de carreras y universidades comunes y reconocidas
        """;

    // Constantes para parsing
    private static final String UNIVERSITIES_SECTION_HEADER = "UNIVERSIDADES RECOMENDADAS:";
    private static final String CAREERS_SECTION_HEADER = "CARRERAS RECOMENDADAS:";
    private static final String LINE_PREFIX = "-";
    private static final String DEFAULT_PROXIMITY = "No especificado";
    private static final String DEFAULT_LOCATION = "Ubicación no especificada";

    public CareerUniversityService(ChatSessionRepository chatSessionRepository,
                                   OpenRouterService openRouterService,
                                   ImageSearchService imageSearchService) {
        this.chatSessionRepository = chatSessionRepository;
        this.openRouterService = openRouterService;
        this.imageSearchService = imageSearchService;
    }

    public CareerUniversityResponse getCareersAndUniversities(String userId) {
        ChatSession session = getActiveUserSession(userId);
        List<Message> conversationHistory = session.getMessages();

        String llmResponse = openRouterService.getCareerUniversityRecommendation(CAREER_PROMPT, conversationHistory);

        CareerUniversityResponse response = parseLLMResponse(llmResponse);
        response.setSessionId(session.getId());

        return response;
    }

    private ChatSession getActiveUserSession(String userId) {
        return chatSessionRepository
                .findFirstByUserIdOrderByUpdatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("No se encontró sesión activa para el usuario"));
    }

    private CareerUniversityResponse parseLLMResponse(String llmResponse) {
        if (llmResponse == null || llmResponse.trim().isEmpty()) {
            return new CareerUniversityResponse(new ArrayList<>(), new ArrayList<>(), null);
        }

        String[] sections = llmResponse.split(UNIVERSITIES_SECTION_HEADER);

        List<CareerResponse> careers = parseCareersSection(sections[0]);
        List<UniversityResponse> universities = sections.length > 1 ?
                parseUniversitiesSection(sections[1]) : new ArrayList<>();

        return new CareerUniversityResponse(careers, universities, null);
    }

    private List<CareerResponse> parseCareersSection(String careersSection) {
        List<CareerResponse> careers = new ArrayList<>();
        String[] lines = careersSection.split("\n");

        for (String line : lines) {
            if (isValidResponseLine(line)) {
                parseCareerLine(line).ifPresent(careers::add);
            }
        }

        return careers;
    }

    private List<UniversityResponse> parseUniversitiesSection(String universitiesSection) {
        List<UniversityResponse> universities = new ArrayList<>();
        String[] lines = universitiesSection.split("\n");

        for (String line : lines) {
            if (isValidResponseLine(line)) {
                parseUniversityLine(line).ifPresent(universities::add);
            }
        }

        return universities;
    }

    private boolean isValidResponseLine(String line) {
        return line.trim().startsWith(LINE_PREFIX) && line.contains(":");
    }

    private java.util.Optional<CareerResponse> parseCareerLine(String line) {
        try {
            String content = extractLineContent(line);
            String[] parts = content.split(":", 2);

            if (parts.length < 2) {
                return java.util.Optional.empty();
            }

            String careerName = parts[0].trim();
            String rest = parts[1].trim();

            String[] descAndReason = rest.split(" - ", 2);
            String description = descAndReason[0].trim();
            String reason = descAndReason.length > 1 ? descAndReason[1].trim() : "Recomendación basada en el perfil";

            String imageUrl = imageSearchService.findCareerImage(careerName);
            List<String> keywords = imageSearchService.findCareerInfo(careerName)
                    .map(career -> career.getKeywords())
                    .orElse(List.of());

            CareerResponse careerResponse = new CareerResponse(careerName, description, reason, imageUrl);
            careerResponse.setKeywords(keywords);

            return java.util.Optional.of(careerResponse);

        } catch (Exception e) {
            // Log the error and skip this line
            return java.util.Optional.empty();
        }
    }

    private java.util.Optional<UniversityResponse> parseUniversityLine(String line) {
        try {
            String content = extractLineContent(line);

            // Separar proximidad
            int lastDashIndex = content.lastIndexOf(" - ");
            String proximity = DEFAULT_PROXIMITY;
            String mainContent = content;

            if (lastDashIndex != -1) {
                proximity = content.substring(lastDashIndex + 3).trim();
                mainContent = content.substring(0, lastDashIndex).trim();
            }

            // Separar nombre/ubicación de carreras ofrecidas
            String[] nameLocationAndCareers = mainContent.split(":", 2);
            if (nameLocationAndCareers.length < 2) {
                return java.util.Optional.empty();
            }

            String nameLocation = nameLocationAndCareers[0].trim();
            String careersOffered = nameLocationAndCareers[1].replace("Ofrece", "").trim();

            // Separar nombre y ubicación
            String[] nameAndLocation = nameLocation.split(" - ", 2);
            String universityName = nameAndLocation[0].trim();
            String location = nameAndLocation.length > 1 ? nameAndLocation[1].trim() : DEFAULT_LOCATION;

            String imageUrl = imageSearchService.findUniversityImage(universityName);
            String country = imageSearchService.findUniversityInfo(universityName)
                    .map(university -> university.getPais())
                    .orElse("País no especificado");

            UniversityResponse universityResponse = new UniversityResponse(
                    universityName, location, careersOffered, proximity, imageUrl
            );
            universityResponse.setCountry(country);

            return java.util.Optional.of(universityResponse);

        } catch (Exception e) {
            // Log the error and skip this line
            return java.util.Optional.empty();
        }
    }

    private String extractLineContent(String line) {
        return line.trim().substring(1).trim(); // Remover el "-"
    }
}