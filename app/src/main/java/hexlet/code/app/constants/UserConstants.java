package hexlet.code.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConstants {
    public static final int USER_EMAIL_MIN_LENGTH = 3;
    public static final int USER_FIELD_MAX_LENGTH = 50;
}
