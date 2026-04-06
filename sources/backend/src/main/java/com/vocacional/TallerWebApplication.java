package com.vocacional;

import com.vocacional.model.User;
import com.vocacional.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
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
		Dotenv dotenv = Dotenv.configure()
				.filename(".env")
				.directory("./")
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

        System.setProperty("SPRING_DATA_MONGODB_URI", dotenv.get("SPRING_DATA_MONGODB_URI"));
        System.setProperty("OPENROUTER_API_KEY", dotenv.get("OPENROUTER_API_KEY"));
        System.setProperty("APP_LLM_DEFAULT_MODEL", dotenv.get("APP_LLM_DEFAULT_MODEL", "x-ai/grok-4-fast"));
        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT", "8080"));

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
