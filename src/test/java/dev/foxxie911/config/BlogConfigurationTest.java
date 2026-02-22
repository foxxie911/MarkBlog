package dev.foxxie911.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlogConfigurationTest {

    private static void execute() {
        BlogConfiguration config = new BlogConfiguration();
        assertNotNull(config);
    }

    @Test
    void testValidConfiguration() {
         assertDoesNotThrow(BlogConfigurationTest::execute);
    }
    
    @Test
    void testToString() {
        assertDoesNotThrow(() -> {
            BlogConfiguration config = new BlogConfiguration();
            String configString = config.toString();
            assertNotNull(configString);
            assertTrue(configString.contains("BlogConfiguration"));
        });
    }
}
