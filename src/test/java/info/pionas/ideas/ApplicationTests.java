package info.pionas.ideas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Value("${info.app.version}")
    private String appVersion;

    @Test
    void contextLoads() {
        Assertions.assertEquals("1.0.0", appVersion);
    }

}
