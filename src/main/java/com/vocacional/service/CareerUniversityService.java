package com.vocacional.service;

import com.vocacional.model.Career;
import com.vocacional.dto.CareerUniversityResponse;
import com.vocacional.model.University;
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

    public CareerUniversityService(ChatSessionRepository chatSessionRepository, OpenRouterService openRouterService) {
        this.chatSessionRepository = chatSessionRepository;
        this.openRouterService = openRouterService;
    }

    private String CAREER_PROMPT = """
        Hasta ahora ¿Qué carreras son más tentativas para el usuario?, realizar un listado de cada carrera. 
        También en base a la ubicación descrita por el usuario ¿Qué universidades cerca de esa ubicación tienen disponibles esas carreras?, 
        realizar un listado de cada universidad.
        
        FORMATO DE RESPUESTA OBLIGATORIO:
        
        CARRERAS RECOMENDADAS:
        - [Nombre de carrera 1]: [Breve descripción] - [Razón de la recomendación]
        - [Nombre de carrera 2]: [Breve descripción] - [Razón de la recomendación]
        - [Nombre de carrera 3]: [Breve descripción] - [Razón de la recomendación]
        
        UNIVERSIDADES RECOMENDADAS:
        - [Nombre universidad 1] - [Ubicación]: Ofrece [carreras ofrecidas] - [Distancia/proximidad]
        - [Nombre universidad 2] - [Ubicación]: Ofrece [carreras ofrecidas] - [Distancia/proximidad]
        - [Nombre universidad 3] - [Ubicación]: Ofrece [carreras ofrecidas] - [Distancia/proximidad]
        
        IMPORTANTE: 
        - Solo listar carreras y universidades basadas en la conversación anterior
        - Si no hay suficiente información sobre ubicación, sugerir universidades genéricas
        - Mantener el formato exacto especificado
        """;

    public CareerUniversityResponse getCareersAndUniversities(String userId) {
        // Obtener la sesión activa del usuario
        ChatSession session = chatSessionRepository
                .findFirstByUserIdOrderByUpdatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("No se encontró sesión activa para el usuario"));

        // Obtener el historial de conversación
        List<Message> conversationHistory = session.getMessages();

        // Obtener respuesta del LLM
        String llmResponse = openRouterService.getCareerUniversityRecommendation(CAREER_PROMPT, conversationHistory);

        // Parsear la respuesta para extraer carreras y universidades
        CareerUniversityResponse response = parseLLMResponse(llmResponse);
        response.setSessionId(session.getId());

        return response;
    }

    private CareerUniversityResponse parseLLMResponse(String llmResponse) {
        List<Career> careers = new ArrayList<>();
        List<University> universities = new ArrayList<>();

        if (llmResponse != null && !llmResponse.isEmpty()) {
            String[] sections = llmResponse.split("UNIVERSIDADES RECOMENDADAS:");

            if (sections.length > 0) {
                careers = parseCareersSection(sections[0]);
            }

            if (sections.length > 1) {
                universities = parseUniversitiesSection(sections[1]);
            }
        }

        return new CareerUniversityResponse(careers, universities, null);
    }

    private List<Career> parseCareersSection(String careersSection) {
        List<Career> careers = new ArrayList<>();
        String[] lines = careersSection.split("\n");

        for (String line : lines) {
            if (line.trim().startsWith("-") && line.contains(":")) {
                // Formato: - [Nombre]: [Descripción] - [Razón]
                String content = line.trim().substring(1).trim(); // Remover el "-"
                String[] parts = content.split(":", 2);

                if (parts.length >= 2) {
                    String careerName = parts[0].trim();
                    String rest = parts[1].trim();

                    // Separar descripción y razón
                    String[] descAndReason = rest.split(" - ", 2);
                    String description = descAndReason[0].trim();
                    String reason = descAndReason.length > 1 ? descAndReason[1].trim() : "Recomendación basada en el perfil";

                    careers.add(new Career(careerName, description, reason));
                }
            }
        }

        return careers;
    }

    private List<University> parseUniversitiesSection(String universitiesSection) {
        List<University> universities = new ArrayList<>();
        String[] lines = universitiesSection.split("\n");

        for (String line : lines) {
            if (line.trim().startsWith("-") && line.contains(":")) {
                // Formato: - [Nombre] - [Ubicación]: Ofrece [carreras] - [Proximidad]
                String content = line.trim().substring(1).trim(); // Remover el "-"

                // Buscar el último " - " para separar proximidad
                int lastDashIndex = content.lastIndexOf(" - ");
                String proximity = "No especificado";
                String mainContent = content;

                if (lastDashIndex != -1) {
                    proximity = content.substring(lastDashIndex + 3).trim();
                    mainContent = content.substring(0, lastDashIndex).trim();
                }

                // Separar nombre/ubicación de carreras ofrecidas
                String[] nameLocationAndCareers = mainContent.split(":", 2);
                if (nameLocationAndCareers.length >= 2) {
                    String nameLocation = nameLocationAndCareers[0].trim();
                    String careersOffered = nameLocationAndCareers[1].replace("Ofrece", "").trim();

                    // Separar nombre y ubicación
                    String[] nameAndLocation = nameLocation.split(" - ", 2);
                    String name = nameAndLocation[0].trim();
                    String location = nameAndLocation.length > 1 ? nameAndLocation[1].trim() : "Ubicación no especificada";

                    universities.add(new University(name, location, careersOffered, proximity));
                }
            }
        }

        return universities;
    }
}