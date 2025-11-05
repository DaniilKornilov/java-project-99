package hexlet.code.security;

import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("DesignForExtension")
public class UserUtils {
    private final UserRepository userRepository;

    public boolean isUser(Long id) {
        var userEmail = userRepository.findById(id).orElseThrow().getEmail();
        var authenticationEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userEmail.equals(authenticationEmail);
    }
}
