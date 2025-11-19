package com.vocacional.controller;

import com.vocacional.dto.*;
import com.vocacional.model.ChatSession;
import com.vocacional.model.User;
import com.vocacional.repository.UserRepository;
import com.vocacional.service.CareerUniversityService;
import com.vocacional.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    private final UserRepository userRepository;

    private final CareerUniversityService careerUniversityService;

    public ChatController(ChatService chatService, UserRepository userRepository, CareerUniversityService careerUniversityService) {
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.careerUniversityService = careerUniversityService;
    }

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(
            @RequestBody ChatRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Buscar tu entidad User manualmente
        com.vocacional.model.User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ChatResponse response = chatService.processMessage(
                request.getMessage(),
                user.getId()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<ChatSession> getCurrentSession(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener la sesión activa más reciente
        ChatSession session = chatService.getOrCreateActiveSession(user.getId());
        return ResponseEntity.ok(session);
    }

    @GetMapping("/careers-and-universities")
    public ResponseEntity<CareerUniversityResponse> getCareersAndUniversities(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            CareerUniversityResponse response = careerUniversityService.getCareersAndUniversities(user.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CareerUniversityResponse(
                            List.of(),
                            List.of(),
                            "Error: " + e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/session")
    public ResponseEntity<Void> clearCurrentSession(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean cleared = chatService.deleteSession(user.getId());

        if (!cleared) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay mensajes para borrar en la sesión actual");
        }

        return ResponseEntity.ok().build();
    }

}
