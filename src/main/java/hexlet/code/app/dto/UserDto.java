package hexlet.code.app.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(Long id,
                      String email,
                      String firstName,
                      String lastName,
                      LocalDateTime createdAt) {
}
