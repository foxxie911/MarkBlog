package dev.foxxie911.config;

import dev.foxxie911.exception.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BlogConfigurationTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    void testValidConfiguration() {
         assertDoesNotThrow(() -> {
             BlogConfiguration config = new BlogConfiguration();
             assertNotNull(config);
         });
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