package hexlet.code.dto;

import java.time.LocalDateTime;

public record LabelDto(Long id,
                       String name,
                       LocalDateTime createdAt) {

}
