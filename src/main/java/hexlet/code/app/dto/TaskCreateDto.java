package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskCreateDto(Integer index,
                            Long assigneeId,
                            @NotBlank String title,
                            String content,
                            @NotBlank String status) {

}
