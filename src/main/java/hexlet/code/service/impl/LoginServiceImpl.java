package hexlet.code.service.impl;

import hexlet.code.dto.LoginDto;
import hexlet.code.security.JwtUtils;
import hexlet.code.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Override
    public String login(LoginDto loginDto) {
        UserDetails user = (UserDetails) authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password())).getPrincipal();
        return jwtUtils.generateToken(user);
    }
}
