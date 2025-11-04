package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;

public record LabelUpdateDto(@NotBlank String name) {
}
