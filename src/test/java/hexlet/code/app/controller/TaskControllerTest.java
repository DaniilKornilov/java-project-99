package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskCreateDto;
import hexlet.code.app.dto.TaskStatusCreateDto;
import hexlet.code.app.dto.TaskUpdateDto;
import hexlet.code.app.dto.UserCreateDto;
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
class TaskControllerTest extends AppApplicationTest {

    @SneakyThrows
    private Long createUserAndReturnId() {
        UserCreateDto dto = new UserCreateDto("jack@google.com", "Jack", "Jons", "pwd123");
        String response = getMockMvc().perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        return getObjectMapper().readTree(response).get("id").asLong();
    }

    @SneakyThrows
    private String createTaskStatusAndReturnSlug() {
        TaskStatusCreateDto dto = new TaskStatusCreateDto("new-name", "new-slug");
        String response = getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        return getObjectMapper().readTree(response).get("slug").asText();
    }

    @Test
    @SneakyThrows
    void shouldCreateTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug);

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.index").value(1))
                .andExpect(jsonPath("$.content").value("New content"))
                .andExpect(jsonPath("$.status").value(slug))
                .andExpect(jsonPath("$.assigneeId").value(assigneeId))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldReturnTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug);

        String response = getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.index").value(1))
                .andExpect(jsonPath("$.content").value("New content"))
                .andExpect(jsonPath("$.status").value(slug))
                .andExpect(jsonPath("$.assigneeId").value(assigneeId))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldReturnTaskList() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto task1 = new TaskCreateDto(1, assigneeId, "New Title 1", "New content 1", slug);
        TaskCreateDto task2 = new TaskCreateDto(2, assigneeId, "New Title 2", "New content 2", slug);

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(task1)))
                .andExpect(status().isCreated());

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(task2)))
                .andExpect(status().isCreated());

        getMockMvc().perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("New Title 1"))
                .andExpect(jsonPath("$[0].index").value(1))
                .andExpect(jsonPath("$[0].content").value("New content 1"))
                .andExpect(jsonPath("$[0].status").value(slug))
                .andExpect(jsonPath("$[0].assigneeId").value(assigneeId))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].title").value("New Title 2"))
                .andExpect(jsonPath("$[1].index").value(2))
                .andExpect(jsonPath("$[1].content").value("New content 2"))
                .andExpect(jsonPath("$[1].status").value(slug))
                .andExpect(jsonPath("$[1].assigneeId").value(assigneeId))
                .andExpect(jsonPath("$[1].createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldUpdateTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug);

        String response = getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        TaskUpdateDto updateDto = new TaskUpdateDto(2, null, "New Title Updated", null, null);

        getMockMvc().perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Title Updated"))
                .andExpect(jsonPath("$.index").value(2))
                .andExpect(jsonPath("$.content").value("New content"))
                .andExpect(jsonPath("$.status").value(slug))
                .andExpect(jsonPath("$.assigneeId").value(assigneeId))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldDeleteTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug);

        String response = getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(delete("/api/tasks/{id}", id))
                .andExpect(status().isNoContent());

        getMockMvc().perform(get("/api/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldNotCreateTaskWithInvalidSlug() {
        Long assigneeId = createUserAndReturnId();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", "invalid");

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
