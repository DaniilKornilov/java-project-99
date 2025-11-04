package hexlet.code.app.controller;

import hexlet.code.app.dto.LoginDto;
import hexlet.code.app.entity.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends AppApplicationTest {

    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.username}")
    private String adminUsername;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    final void registerAdmin() {
        if (getUserRepository().findByEmail(adminUsername).isEmpty()) {
            User user = new User();
            user.setEmail(adminUsername);
            user.setPassword(passwordEncoder.encode(adminPassword));
            getUserRepository().save(user);
        }
    }

    @SneakyThrows
    private String loginAndGetToken(String email, String password) {
        LoginDto request = new LoginDto(email, password);

        return getMockMvc().perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void shouldReturnToken() {
        String token = loginAndGetToken(adminUsername, adminPassword);
        assertThat(token).contains(".");
    }

    @Test
    @SneakyThrows
    void shouldReturn401InvalidPassword() {
        LoginDto request = new LoginDto(adminUsername, "wrong");
        getMockMvc().perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void shouldReturn401UserUnknown() {
        LoginDto request = new LoginDto("unknown@google.com", adminPassword);
        getMockMvc().perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void shouldReturn401Unauthorized() {
        getMockMvc().perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void shouldReturns200ForAdmin() {
        String token = loginAndGetToken(adminUsername, adminPassword);
        getMockMvc().perform(get("/api/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldReturns200ForUser() {
        User user = new User();
        user.setEmail("user@user.ru");
        user.setPassword(passwordEncoder.encode("userpass"));
        getUserRepository().save(user);

        String token = loginAndGetToken("user@user.ru", "userpass");
        getMockMvc().perform(get("/api/task_statuses")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        getMockMvc().perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldReturn403NotEnoughRights() {
        User other = new User();
        other.setEmail("petr@google.com");
        other.setPassword(passwordEncoder.encode("pwd123"));
        getUserRepository().save(other);

        String userToken = loginAndGetToken("petr@google.com", "pwd123");

        getMockMvc().perform(delete("/api/users/{id}", other.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void shouldReturn204EnoughRights() {
        String adminToken = loginAndGetToken(adminUsername, adminPassword);

        User victim = new User();
        victim.setEmail("victim@google.com");
        victim.setPassword(passwordEncoder.encode("pwd123"));
        getUserRepository().save(victim);

        getMockMvc().perform(delete("/api/users/{id}", victim.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
