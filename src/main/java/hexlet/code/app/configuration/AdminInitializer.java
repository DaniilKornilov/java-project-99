package hexlet.code.app.configuration;

import hexlet.code.app.entity.User;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    @Value("${admin.password}")
    private String password;
    @Value("${admin.username}")
    private String username;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public final void run(String... args) {
        if (userRepository.findByEmail(username).isEmpty()) {
            User admin = User.builder()
                    .email(username)
                    .password(passwordEncoder.encode(password))
                    .firstName("Admin")
                    .lastName("User")
                    .build();
            userRepository.save(admin);
        }
    }
}
