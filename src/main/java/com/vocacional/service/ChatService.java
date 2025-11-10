package com.vocacional.service;

import com.vocacional.model.ChatSession;
import com.vocacional.model.Message;
import com.vocacional.repository.ChatSessionRepository;
import com.vocacional.dto.ChatResponse;
import com.vocacional.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ChatService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OpenRouterService openRouterService;

    // Configuración por defecto - fácil de cambiar
    @Value("${app.llm.default-model}")
    private String defaultModel;


    public ChatResponse processMessage(String userMessage, String userId) {

        // Obtener o crear sesión activa
        ChatSession session = getOrCreateActiveSession(userId);

        // Agregar mensaje del usuario
        Message userMsg = new Message();
        userMsg.setContent(userMessage);
        userMsg.setType(Message.MessageType.USER);
        session.getMessages().add(userMsg);

        // Obtener el historial completo de mensajes para contexto
        List<Message> conversationHistory = session.getMessages();

        // Obtener respuesta del LLM seleccionado
        String llmResponse = openRouterService.getVocationalCoachResponse(userMessage, defaultModel, conversationHistory);

        // Agregar respuesta del sistema con metadata del modelo usado
        Message systemMsg = new Message();
        systemMsg.setContent(llmResponse);
        systemMsg.setType(Message.MessageType.SYSTEM);
        systemMsg.setTimestamp(LocalDateTime.now());

        // Metadata para tracking futuro (extensible)
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("model", defaultModel);
        metadata.put("timestamp", LocalDateTime.now().toString());
        systemMsg.setMetadata(metadata);

        session.getMessages().add(systemMsg);
        session.setUpdatedAt(LocalDateTime.now());

        // Guardar metadata de la sesión con el modelo usado
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

}