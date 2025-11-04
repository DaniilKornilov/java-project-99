package hexlet.code.controller;

import hexlet.code.dto.TaskStatusCreateDto;
import hexlet.code.dto.TaskStatusUpdateDto;
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
class TaskStatusControllerTest extends AppApplicationTest {

    @Test
    @SneakyThrows
    void shouldCreateTaskStatus() {
        TaskStatusCreateDto dto = new TaskStatusCreateDto("name-new", "slug-new");

        getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.slug").value("slug-new"))
                .andExpect(jsonPath("$.name").value("name-new"));
    }

    @Test
    @SneakyThrows
    void shouldReturnTaskStatus() {
        TaskStatusCreateDto dto = new TaskStatusCreateDto("name-new", "slug-new");
        String response = getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(get("/api/task_statuses/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.slug").value("slug-new"))
                .andExpect(jsonPath("$.name").value("name-new"));
    }

    @Test
    @SneakyThrows
    void shouldReturnTaskStatusList() {
        TaskStatusCreateDto taskStatus1 = new TaskStatusCreateDto("name1", "slug1");
        TaskStatusCreateDto taskStatus2 = new TaskStatusCreateDto("name2", "slug2");

        getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(taskStatus1)))
                .andExpect(status().isCreated());

        getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(taskStatus2)))
                .andExpect(status().isCreated());

        getMockMvc().perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].slug").value("slug1"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].slug").value("slug2"))
                .andExpect(jsonPath("$[1].createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldUpdateTaskStatus() {
        TaskStatusCreateDto dto = new TaskStatusCreateDto("name1", "slug1");
        String response = getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        TaskStatusUpdateDto updateDto = new TaskStatusUpdateDto("name1updated", null);

        getMockMvc().perform(put("/api/task_statuses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name1updated"))
                .andExpect(jsonPath("$.slug").value("slug1"));
    }

    @Test
    @SneakyThrows
    void shouldDeleteTaskStatus() {
        TaskStatusCreateDto dto = new TaskStatusCreateDto("name1", "slug1");
        String response = getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(delete("/api/task_statuses/{id}", id))
                .andExpect(status().isNoContent());

        getMockMvc().perform(get("/api/task_statuses/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldNotCreateTaskStatusWithInvalidName() {
        TaskStatusCreateDto dto = new TaskStatusCreateDto("", "slug1");

        getMockMvc().perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }
}
