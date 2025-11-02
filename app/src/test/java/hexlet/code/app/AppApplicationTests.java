package hexlet.code.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AppApplicationTests {

    @Test
    void contextLoads() {
        // Test to ensure the application context loads successfully
    }

}
