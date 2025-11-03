package hexlet.code.app.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConstants {
    public static final int USER_EMAIL_MIN_LENGTH = 3;
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}
