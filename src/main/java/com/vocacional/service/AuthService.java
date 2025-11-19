package com.vocacional.service;

import com.vocacional.dto.AuthResponse;
import com.vocacional.dto.LoginRequest;
import com.vocacional.dto.RegisterRequest;
import com.vocacional.model.User;
import com.vocacional.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse registerUser(RegisterRequest request) {
        try {
            // Validaciones básicas
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return new AuthResponse("El nombre de usuario es requerido", null, null, false);
            }

            if (request.getPassword() == null || request.getPassword().length() < 4) {
                return new AuthResponse("La contraseña debe tener al menos 4 caracteres", null, null, false);
            }

            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return new AuthResponse("El email es requerido", null, null, false);
            }

            // Verificar si el usuario ya existe
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return new AuthResponse("El nombre de usuario ya está en uso", null, null, false);
            }

            // Verificar si el email ya existe
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return new AuthResponse("El email ya está registrado", null, null, false);
            }

            // Crear nuevo usuario
            User newUser = new User();
            newUser.setUsername(request.getUsername().trim());
            newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
            newUser.setEmail(request.getEmail().trim().toLowerCase());
            newUser.setCreatedAt(LocalDateTime.now());

            User savedUser = userRepository.save(newUser);

            return new AuthResponse(
                    "Usuario registrado exitosamente",
                    savedUser.getUsername(),
                    savedUser.getId(),
                    true
            );

        } catch (Exception e) {
            return new AuthResponse("Error al registrar usuario: " + e.getMessage(), null, null, false);
        }
    }

    public AuthResponse loginUser(LoginRequest request) {
        try {
            // Validaciones básicas
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return new AuthResponse("El nombre de usuario es requerido", null, null, false);
            }

            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return new AuthResponse("La contraseña es requerida", null, null, false);
            }

            // Buscar usuario
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername().trim());
            if (userOpt.isEmpty()) {
                return new AuthResponse("Usuario o contraseña incorrectos", null, null, false);
            }

            User user = userOpt.get();

            // Verificar contraseña
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse("Usuario o contraseña incorrectos", null, null, false);
            }

            userRepository.save(user);

            return new AuthResponse(
                    "Login exitoso",
                    user.getUsername(),
                    user.getId(),
                    true
            );

        } catch (Exception e) {
            return new AuthResponse("Error en el login: " + e.getMessage(), null, null, false);
        }
    }
}