package com.vocacional;

import com.vocacional.model.User;
import com.vocacional.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class TallerWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerWebApplication.class, args);
	}

    private final PasswordEncoder passwordEncoder;

    public TallerWebApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Profile("!prod") // Solo en desarrollo
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Usuario admin por defecto
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setEmail("admin@vocacional.com");
                admin.setCreatedAt(LocalDateTime.now());
                userRepository.save(admin);
            }

        };
    }
}
