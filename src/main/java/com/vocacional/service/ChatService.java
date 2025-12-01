package com.vocacional.service;

import com.vocacional.model.ChatSession;
import com.vocacional.model.Message;
import com.vocacional.repository.ChatSessionRepository;
import com.vocacional.dto.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;

    private final OpenRouterService openRouterService;

    // Configuración por defecto - fácil de cambiar
    private final String defaultModel;

    public ChatService(ChatSessionRepository chatSessionRepository,
                       OpenRouterService openRouterService,
                       @Value("${app.llm.default-model}") String defaultModel) {
        this.chatSessionRepository = chatSessionRepository;
        this.openRouterService = openRouterService;
        this.defaultModel = defaultModel;
    }


    public ChatResponse processMessage(String userMessage, String userId) {

        ChatSession session = getOrCreateActiveSession(userId);

        Message userMsg = new Message();
        userMsg.setContent(userMessage);
        userMsg.setType(Message.MessageType.USER);
        session.getMessages().add(userMsg);

        List<Message> conversationHistory = session.getMessages();

        String llmResponse = openRouterService.getVocationalCoachResponse(userMessage, defaultModel, conversationHistory);

        Message systemMsg = new Message();
        systemMsg.setContent(llmResponse);
        systemMsg.setType(Message.MessageType.SYSTEM);
        systemMsg.setTimestamp(LocalDateTime.now());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("model", defaultModel);
        metadata.put("timestamp", LocalDateTime.now().toString());
        systemMsg.setMetadata(metadata);

        session.getMessages().add(systemMsg);
        session.setUpdatedAt(LocalDateTime.now());

        if (session.getMetadata() == null) {
            session.setMetadata(new HashMap<>());
        }
        session.getMetadata().put("lastModelUsed", defaultModel);
        session.getMetadata().put("messageCount", session.getMessages().size());

        chatSessionRepository.save(session);

        return new ChatResponse(llmResponse, session.getId(), defaultModel, session.getMessages().size());
    }

    public ChatSession getOrCreateActiveSession(String userId) {
        return chatSessionRepository
                .findFirstByUserIdOrderByUpdatedAtDesc(userId)
                .orElseGet(() -> createNewSession(userId));
    }

    private ChatSession createNewSession(String userId) {
        ChatSession newSession = new ChatSession();
        newSession.setUserId(userId);
        newSession.setTitle("Sesión " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        newSession.setMessages(new ArrayList<>());
        return chatSessionRepository.save(newSession);
    }

    public boolean deleteSession(String userId) {
        try {
            ChatSession chatSession = getOrCreateActiveSession(userId);
            chatSessionRepository.delete(chatSession);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar la sesión: " + e.getMessage(), e);
        }
    }
}