package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;

public record LabelCreateDto(@NotBlank String name) {
}
