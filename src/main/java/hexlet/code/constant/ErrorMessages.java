package hexlet.code.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessages {
    public static final String USER_NOT_FOUND = "User with id %s not found";
    public static final String TASK_STATUS_NOT_FOUND = "Task status with id %s not found";
    public static final String TASK_NOT_FOUND = "Task with id %s not found";
    public static final String LABEL_NOT_FOUND = "Label with id %s not found";
}
