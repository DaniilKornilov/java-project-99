package hexlet.code.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskDto(Long id,
                      Integer index,
                      LocalDateTime createdAt,
                      Long assigneeId,
                      String title,
                      String content,
                      String status,
                      Set<Long> taskLabelIds) {
}
