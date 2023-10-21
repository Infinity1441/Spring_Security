package dev.bakhtigul.booking;

import dev.bakhtigul.booking.config.security.SessionUser;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class Application {
    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AuditorAware<Long> auditorAware(SessionUser sessionUser) {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
            return Optional.of(sessionUser.id());
        };
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        Path path = Path.of(env.getRequiredProperty("application.log.path"));
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}