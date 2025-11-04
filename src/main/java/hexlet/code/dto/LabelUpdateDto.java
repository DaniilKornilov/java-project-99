package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;

public record LabelUpdateDto(@NotBlank String name) {
}
