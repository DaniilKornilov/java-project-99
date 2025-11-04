package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;

public record LabelCreateDto(@NotBlank String name) {
}
