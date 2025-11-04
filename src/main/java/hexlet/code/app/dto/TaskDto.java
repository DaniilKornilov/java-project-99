package hexlet.code.app.dto;

import java.time.LocalDateTime;

public record TaskDto(Long id,
                      Integer index,
                      LocalDateTime createdAt,
                      Long assigneeId,
                      String title,
                      String content,
                      String status) {
}
