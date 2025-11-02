package hexlet.code.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import static hexlet.code.app.constants.UserConstants.USER_EMAIL_MIN_LENGTH;

public record UserUpdateDto(@Email String email,
                            String firstName,
                            String lastName,
                            @Size(min = USER_EMAIL_MIN_LENGTH) String password) {
}
