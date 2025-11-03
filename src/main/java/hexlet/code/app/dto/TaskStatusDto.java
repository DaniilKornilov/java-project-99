package hexlet.code.app.dto;

import java.time.LocalDateTime;

public record TaskStatusDto(Long id,
                            String name,
                            String slug,
                            LocalDateTime createdAt
) {
}
