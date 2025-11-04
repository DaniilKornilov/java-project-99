package hexlet.code.service;

import hexlet.code.dto.LoginDto;
import hexlet.code.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class LoginService {
    private final UserDetailsService databaseUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public String login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

        UserDetails user = databaseUserDetailsService.loadUserByUsername(loginDto.username());
        return jwtUtils.generateToken(user);
    }
}
