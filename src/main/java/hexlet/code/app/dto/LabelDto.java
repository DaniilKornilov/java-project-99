package hexlet.code.app.dto;

import java.time.LocalDateTime;

public record LabelDto(Long id,
                       String name,
                       LocalDateTime createdAt) {

}
