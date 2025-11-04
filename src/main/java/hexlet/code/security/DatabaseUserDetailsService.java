package hexlet.code.security;

import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static hexlet.code.constant.UserConstants.ADMIN;
import static hexlet.code.constant.UserConstants.USER;
import static org.springframework.security.core.userdetails.User.withUsername;

@Component
@RequiredArgsConstructor
public final class DatabaseUserDetailsService implements UserDetailsService {
    private static final String ROLE_PREFIX = "ROLE_";

    @Value("${admin.username}")
    private String username;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String authorities;
        if (user.getEmail().equals(username)) {
            authorities = ROLE_PREFIX + ADMIN;
        } else {
            authorities = ROLE_PREFIX + USER;
        }

        return withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
