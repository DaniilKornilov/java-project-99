package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskStatusCreateDto(@NotBlank String name,
                                  @NotBlank String slug) {
}
