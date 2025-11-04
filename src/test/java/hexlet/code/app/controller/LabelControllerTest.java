package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelCreateDto;
import hexlet.code.app.dto.LabelUpdateDto;
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
class LabelControllerTest extends AppApplicationTest {

    @Test
    @SneakyThrows
    void shouldCreateLabel() {
        LabelCreateDto dto = new LabelCreateDto("name-new");

        getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("name-new"));
    }

    @Test
    @SneakyThrows
    void shouldReturnLabel() {
        LabelCreateDto dto = new LabelCreateDto("name-new");
        String response = getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(get("/api/labels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("name-new"));
    }

    @Test
    @SneakyThrows
    void shouldReturnLabelsList() {
        LabelCreateDto label1 = new LabelCreateDto("name1");
        LabelCreateDto label2 = new LabelCreateDto("name2");

        getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(label1)))
                .andExpect(status().isCreated());

        getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(label2)))
                .andExpect(status().isCreated());

        getMockMvc().perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].createdAt").exists());
    }

    @Test
    @SneakyThrows
    void shouldUpdateLabel() {
        LabelCreateDto dto = new LabelCreateDto("name1");
        String response = getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        LabelUpdateDto updateDto = new LabelUpdateDto("name1updated");

        getMockMvc().perform(put("/api/labels/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name1updated"));
    }

    @Test
    @SneakyThrows
    void shouldDeleteLabel() {
        LabelCreateDto dto = new LabelCreateDto("name1");
        String response = getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        Long id = getObjectMapper().readTree(response).get("id").asLong();

        getMockMvc().perform(delete("/api/labels/{id}", id))
                .andExpect(status().isNoContent());

        getMockMvc().perform(get("/api/labels/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldNotCreateLabelWithInvalidName() {
        LabelCreateDto dto = new LabelCreateDto("");

        getMockMvc().perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }
}
