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
    private final String apiKey;
    private final String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

    // Constantes de configuración
    private static final int MAX_CONTEXT_TOKENS = 4000;
    private static final int MAX_RESPONSE_TOKENS = 500;
    private static final int MAX_CAREER_RESPONSE_TOKENS = 800;
    private static final double TOKENS_PER_WORD = 1.3;

    @Value("${app.llm.default-model}")
    private String defaultModel;

    public OpenRouterService(@Value("${openrouter.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public String getVocationalCoachResponse(String userMessage, String model, List<Message> conversationHistory) {
        List<OpenRouterRequest.Message> messages = buildMessagesWithHistory(userMessage, conversationHistory);
        return callOpenRouterAPI(model, messages, MAX_RESPONSE_TOKENS);
    }

    public String getCareerUniversityRecommendation(String prompt, List<Message> conversationHistory) {
        List<OpenRouterRequest.Message> messages = buildMessagesForCareerAnalysis(prompt, conversationHistory);
        return callOpenRouterAPI(defaultModel, messages, MAX_CAREER_RESPONSE_TOKENS);
    }

    private String callOpenRouterAPI(String model, List<OpenRouterRequest.Message> messages, int maxTokens) {
        OpenRouterRequest request = createRequest(model, messages, maxTokens);
        HttpEntity<OpenRouterRequest> entity = createHttpEntity(request);

        try {
            ResponseEntity<OpenRouterResponse> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, OpenRouterResponse.class
            );

            return extractResponseContent(response);

        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenRouter API: " + e.getMessage(), e);
        }
    }

    private OpenRouterRequest createRequest(String model, List<OpenRouterRequest.Message> messages, int maxTokens) {
        OpenRouterRequest request = new OpenRouterRequest();
        request.setModel(model);
        request.setMessages(messages);
        request.setMax_tokens(maxTokens);
        return request;
    }

    private HttpEntity<OpenRouterRequest> createHttpEntity(OpenRouterRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        return new HttpEntity<>(request, headers);
    }

    private String extractResponseContent(ResponseEntity<OpenRouterResponse> response) {
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        } else {
            throw new RuntimeException("Error en la respuesta de OpenRouter: " + response.getStatusCode());
        }
    }

    private List<OpenRouterRequest.Message> buildMessagesWithHistory(String newUserMessage, List<Message> conversationHistory) {
        List<OpenRouterRequest.Message> messages = new ArrayList<>();

        // System prompt inicial
        String systemPrompt = buildSystemPrompt();
        messages.add(createMessage("system", systemPrompt));

        int estimatedTokens = estimateTokens(systemPrompt);

        // Agregar historial de conversación
        if (conversationHistory != null && conversationHistory.size() > 1) {
            List<Message> recentHistory = getRecentMessagesWithinLimit(
                    conversationHistory, MAX_CONTEXT_TOKENS - MAX_RESPONSE_TOKENS - estimatedTokens
            );

            for (Message msg : recentHistory) {
                String role = getRoleFromMessageType(msg.getType());
                messages.add(createMessage(role, msg.getContent()));
                estimatedTokens += estimateTokens(msg.getContent());
            }
        }

        // Agregar el nuevo mensaje del usuario
        messages.add(createMessage("user", newUserMessage));

        return messages;
    }

    private List<OpenRouterRequest.Message> buildMessagesForCareerAnalysis(String prompt, List<Message> conversationHistory) {
        List<OpenRouterRequest.Message> messages = new ArrayList<>();

        String systemPrompt = buildCareerAnalysisSystemPrompt();
        messages.add(createMessage("system", systemPrompt));

        // Agregar historial de conversación reciente
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            int startIndex = Math.max(0, conversationHistory.size() - 10); // Últimos 10 mensajes
            for (int i = startIndex; i < conversationHistory.size(); i++) {
                Message msg = conversationHistory.get(i);
                String role = getRoleFromMessageType(msg.getType());
                messages.add(createMessage(role, msg.getContent()));
            }
        }

        // Agregar el prompt específico para carreras/universidades
        messages.add(createMessage("user", prompt));

        return messages;
    }

    private String getRoleFromMessageType(Message.MessageType type) {
        return type == Message.MessageType.USER ? "user" : "assistant";
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
            - Saluda solo la primera vez
            
            Enfócate en ayudar al usuario a:
            1. Identificar sus intereses genuinos
            2. Reconocer sus fortalezas y habilidades
            3. Explorar opciones profesionales alineadas
            4. Establecer pasos concretos para avanzar
            """;
    }

    private String buildCareerAnalysisSystemPrompt() {
        return """
            Eres un especialista en orientación vocacional y conocimiento de universidades. 
            Analiza la conversación y proporciona recomendaciones específicas de carreras y universidades.
            Mantén el formato exacto solicitado y sé preciso en tus recomendaciones.
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
        int wordCount = text.split("\\s+").length;
        return (int) Math.ceil(wordCount * TOKENS_PER_WORD);
    }
}