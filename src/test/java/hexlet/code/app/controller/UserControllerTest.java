package hexlet.code.app.controller;

import hexlet.code.app.dto.UserCreateDto;
import hexlet.code.app.dto.UserUpdateDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest extends AppApplicationTest {

    @Test
    @SneakyThrows
    void shouldCreateUser() {
        UserCreateDto dto = new UserCreateDto("john@google.com", "John", "Doe", "secret");

        getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("john@google.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @SneakyThrows
    void shouldReturnUser() {
        UserCreateDto dto = new UserCreateDto("jack@google.com", "Jack", "Jons", "pwd123");
        String response = getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value("jack@google.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @SneakyThrows
    void shouldReturnUserList() {
        UserCreateDto user1 = new UserCreateDto("john@google.com", "John", "Doe", "secret");
        UserCreateDto user2 = new UserCreateDto("jack@yahoo.com", "Jack", "Jons", "password");

        getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(user1)))
                .andExpect(status().isCreated());

        getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(user2)))
                .andExpect(status().isCreated());

        getMockMvc().perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value("john@google.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].email").value("jack@yahoo.com"))
                .andExpect(jsonPath("$[1].firstName").value("Jack"))
                .andExpect(jsonPath("$[1].lastName").value("Jons"))
                .andExpect(jsonPath("$[1].createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldUpdateUser() {
        UserCreateDto dto = new UserCreateDto("anna@google.com", "Anna", "Smith", "pwd123");
        String response = getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        UserUpdateDto updateDto = new UserUpdateDto("anna@yahoo.com", null, null, null);

        getMockMvc().perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("anna@yahoo.com"))
                .andExpect(jsonPath("$.firstName").value("Anna"));
    }

    @Test
    @SneakyThrows
    void shouldDeleteUser() {
        UserCreateDto dto = new UserCreateDto("mike@google.com", "Mike", "Tyson", "pwd123");
        String response = getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        getMockMvc().perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldNotCreateUserWithInvalidEmail() {
        UserCreateDto dto = new UserCreateDto("not-an-email", "Bad", "User", "pw");

        getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }
}
