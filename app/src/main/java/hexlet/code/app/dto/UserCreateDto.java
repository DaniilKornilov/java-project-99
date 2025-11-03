package hexlet.code.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static hexlet.code.app.constant.UserConstants.USER_EMAIL_MIN_LENGTH;

public record UserCreateDto(@Email @NotBlank String email,
                            String firstName,
                            String lastName,
                            @NotBlank @Size(min = USER_EMAIL_MIN_LENGTH) String password) {
}
