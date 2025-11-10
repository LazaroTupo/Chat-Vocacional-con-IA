package com.vocacional.repository;

import com.vocacional.model.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {

    // Encontrar sesiones por usuario
    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(String userId);

    // Encontrar sesión activa más reciente
    Optional<ChatSession> findFirstByUserIdOrderByUpdatedAtDesc(String userId);
}