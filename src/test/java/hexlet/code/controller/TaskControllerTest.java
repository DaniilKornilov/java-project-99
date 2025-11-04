package hexlet.code.controller;

import hexlet.code.dto.LabelCreateDto;
import hexlet.code.dto.TaskCreateDto;
import hexlet.code.dto.TaskStatusCreateDto;
import hexlet.code.dto.TaskUpdateDto;
import hexlet.code.dto.UserCreateDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import java.util.Set;

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

    @SneakyThrows
    private Long createLabelAndReturnId() {
        LabelCreateDto dto = new LabelCreateDto("label-name");
        String response = getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        return getObjectMapper().readTree(response).get("id").asLong();
    }

    @Test
    @SneakyThrows
    void shouldCreateTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        Long labelId = createLabelAndReturnId();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug, Set.of(labelId));

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
                .andExpect(jsonPath("$.taskLabelIds").isArray())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldReturnTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug, Set.of());

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
                .andExpect(jsonPath("$.taskLabelIds").isArray())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldReturnTaskList() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto task1 = new TaskCreateDto(1, assigneeId, "New Title 1", "New content 1", slug, Set.of());
        TaskCreateDto task2 = new TaskCreateDto(2, assigneeId, "New Title 2", "New content 2", slug, Set.of());

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
                .andExpect(jsonPath("$[0].taskLabelIds").isArray())
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].title").value("New Title 2"))
                .andExpect(jsonPath("$[1].index").value(2))
                .andExpect(jsonPath("$[1].content").value("New content 2"))
                .andExpect(jsonPath("$[1].status").value(slug))
                .andExpect(jsonPath("$[1].assigneeId").value(assigneeId))
                .andExpect(jsonPath("$[0].taskLabelIds").isArray())
                .andExpect(jsonPath("$[1].createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldReturnFilteredTaskList() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();

        TaskCreateDto task1 = new TaskCreateDto(1, assigneeId, "Create API", "Content 1", slug, Set.of());
        TaskCreateDto task2 = new TaskCreateDto(2, assigneeId, "Fix Bug", "Content 2", slug, Set.of());

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(task1)))
                .andExpect(status().isCreated());

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(task2)))
                .andExpect(status().isCreated());

        getMockMvc().perform(get("/api/tasks")
                        .param("titleCont", "Create")
                        .param("assigneeId", assigneeId.toString())
                        .param("status", slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("Create API"))
                .andExpect(jsonPath("$[0].index").value(1))
                .andExpect(jsonPath("$[0].content").value("Content 1"))
                .andExpect(jsonPath("$[0].status").value(slug))
                .andExpect(jsonPath("$[0].assigneeId").value(assigneeId))
                .andExpect(jsonPath("$[0].taskLabelIds").isArray())
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldUpdateTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug, Set.of());

        String response = getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        Long labelId = createLabelAndReturnId();
        TaskUpdateDto updateDto = new TaskUpdateDto(2, null, "New Title Updated", null, null, Set.of(labelId));

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
                .andExpect(jsonPath("$.taskLabelIds").isArray())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldDeleteTask() {
        Long assigneeId = createUserAndReturnId();
        String slug = createTaskStatusAndReturnSlug();
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", slug, Set.of());

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
        TaskCreateDto dto = new TaskCreateDto(1, assigneeId, "New Title", "New content", "invalid", Set.of());

        getMockMvc().perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
