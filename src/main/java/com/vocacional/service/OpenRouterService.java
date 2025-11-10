package com.vocacional.service;

import com.vocacional.dto.OpenRouterRequest;
import com.vocacional.dto.OpenRouterResponse;
import com.vocacional.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenRouterService {

    private final RestTemplate restTemplate;
    private final int MAX_CONTEXT_TOKENS = 4000;
    private final int MAX_RESPONSE_TOKENS = 500;
    private final String apiKey;
    private final String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${app.llm.default-model}")
    private String defaultModel;


    public OpenRouterService(@Value("${openrouter.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public String getVocationalCoachResponse(String userMessage, String model, List<Message> conversationHistory) {
        List<OpenRouterRequest.Message> messages = buildMessagesWithHistory(userMessage, conversationHistory);

        OpenRouterRequest request = new OpenRouterRequest();
        request.setModel(model);
        request.setMessages(messages);
        request.setMax_tokens(MAX_RESPONSE_TOKENS);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        // headers.set("HTTP-Referer", "https://yourdomain.com"); // Opcional pero recomendado
        headers.set("X-Title", "Vocational Coach"); // Opcional

        HttpEntity<OpenRouterRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<OpenRouterResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    OpenRouterResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            } else {
                throw new RuntimeException("Error en la respuesta de OpenRouter: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenRouter API: " + e.getMessage(), e);
        }
    }

    private List<OpenRouterRequest.Message> buildMessagesWithHistory(String newUserMessage, List<Message> conversationHistory) {
        List<OpenRouterRequest.Message> messages = new ArrayList<>();

        // System prompt inicial
        String systemPrompt = buildSystemPrompt();
        messages.add(createMessage("system", systemPrompt));

        int estimatedTokens = estimateTokens(systemPrompt);

        // Agregar historial de conversación (excluyendo el último mensaje del usuario que acabamos de agregar)
        if (conversationHistory != null && conversationHistory.size() > 1) {
            List<Message> recentHistory = getRecentMessagesWithinLimit(
                    conversationHistory, MAX_CONTEXT_TOKENS - MAX_RESPONSE_TOKENS - estimatedTokens
            );

            for (Message msg : recentHistory) {
                String role = msg.getType() == Message.MessageType.USER ? "user" : "assistant";
                messages.add(createMessage(role, msg.getContent()));
                estimatedTokens += estimateTokens(msg.getContent());
            }
        }

        // Agregar el nuevo mensaje del usuario
        messages.add(createMessage("user", newUserMessage));

        return messages;
    }

    private OpenRouterRequest.Message createMessage(String role, String content) {
        OpenRouterRequest.Message message = new OpenRouterRequest.Message();
        message.setRole(role);
        message.setContent(List.of(createTextContent(content)));
        return message;
    }

    private OpenRouterRequest.Content createTextContent(String text) {
        OpenRouterRequest.Content content = new OpenRouterRequest.Content();
        content.setType("text");
        content.setText(text);
        return content;
    }

    private String buildSystemPrompt() {
        return """
            Eres un coach vocacional especializado. Tu objetivo es ayudar a los usuarios 
            a descubrir su vocación profesional mediante preguntas reflexivas y guiadas.
            
            Características clave:
            - Sé empático y comprensivo
            - Haz preguntas abiertas que fomenten la reflexión
            - Proporciona orientación basada en intereses y habilidades
            - Evita dar respuestas genéricas o predeterminadas
            - Adapta tu enfoque según las respuestas del usuario
            - Mantén un tono profesional pero cercano
            
            Enfócate en ayudar al usuario a:
            1. Identificar sus intereses genuinos
            2. Reconocer sus fortalezas y habilidades
            3. Explorar opciones profesionales alineadas
            4. Establecer pasos concretos para avanzar
            """;
    }

    private List<Message> getRecentMessagesWithinLimit(List<Message> allMessages, int tokenLimit) {
        List<Message> recentMessages = new ArrayList<>();
        int totalTokens = 0;

        // Recorrer desde el más reciente (excluyendo el último que es el nuevo mensaje)
        for (int i = allMessages.size() - 2; i >= 0; i--) {
            Message msg = allMessages.get(i);
            int msgTokens = estimateTokens(msg.getContent());

            if (totalTokens + msgTokens <= tokenLimit) {
                recentMessages.add(0, msg); // Agregar al inicio para mantener orden
                totalTokens += msgTokens;
            } else {
                break; // No caben más mensajes
            }
        }

        return recentMessages;
    }

    private int estimateTokens(String text) {
        if (text == null) return 0;
        // Estimación conservadora: ~1.3 tokens por palabra en español
        int wordCount = text.split("\\s+").length;
        return (int) Math.ceil(wordCount * 1.3);
    }

    public String getCareerUniversityRecommendation(String prompt, List<Message> conversationHistory) {

        List<OpenRouterRequest.Message> messages = buildMessagesForCareerAnalysis(prompt, conversationHistory);

        OpenRouterRequest request = new OpenRouterRequest();
        request.setModel(defaultModel);
        request.setMessages(messages);
        request.setMax_tokens(800); // Más tokens para listados detallados

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "https://vocacional-app.com");
        headers.set("X-Title", "Career University Advisor");

        HttpEntity<OpenRouterRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<OpenRouterResponse> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, OpenRouterResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            } else {
                throw new RuntimeException("Error en OpenRouter: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenRouter API: " + e.getMessage(), e);
        }
    }

    private List<OpenRouterRequest.Message> buildMessagesForCareerAnalysis(
            String prompt, List<Message> conversationHistory) {

        List<OpenRouterRequest.Message> messages = new ArrayList<>();

        // System prompt específico para análisis de carreras
        String systemPrompt = """
            Eres un especialista en orientación vocacional y conocimiento de universidades. 
            Analiza la conversación y proporciona recomendaciones específicas de carreras y universidades.
            Mantén el formato exacto solicitado y sé preciso en tus recomendaciones.
            """;

        messages.add(createMessage("system", systemPrompt));

        // Agregar historial de conversación (sin el último mensaje si es muy reciente)
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            int startIndex = Math.max(0, conversationHistory.size() - 10); // Últimos 10 mensajes
            for (int i = startIndex; i < conversationHistory.size(); i++) {
                Message msg = conversationHistory.get(i);
                String role = msg.getType() == Message.MessageType.USER ? "user" : "assistant";
                messages.add(createMessage(role, msg.getContent()));
            }
        }

        // Agregar el prompt específico para carreras/universidades
        messages.add(createMessage("user", prompt));

        return messages;
    }
}
