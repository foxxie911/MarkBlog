package dev.foxxie911.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dev.foxxie911.BlogGeneratorApplication;

public class BlogGeneratorApplicationIT {
    
    @Test
    void testApplicationStartup() {
        // This is a basic integration test that ensures the application can start
        // In a real scenario, you'd set up proper test configuration and mock dependencies

        // Just testing that the main method doesn't throw immediate exceptions
        // Actual integration testing would require proper test setup
        assertDoesNotThrow(BlogGeneratorApplication::main);
    }
}
