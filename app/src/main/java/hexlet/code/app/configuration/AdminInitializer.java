package hexlet.code.app.configuration;

import hexlet.code.app.entity.User;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("java:S6437")
    public final void run(String... args) {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User admin = User.builder()
                    .email("hexlet@example.com")
                    .password(passwordEncoder.encode("qwerty"))
                    .firstName("Admin")
                    .lastName("User")
                    .build();
            userRepository.save(admin);
        }
    }
}
