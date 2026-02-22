package dev.foxxie911.exception;

/**
 * Exception thrown when there are issues with application configuration.
 * Used when required configuration properties are missing, invalid, or malformed.
 */
public class ConfigurationException extends MarkBlogException {

    /**
     * Constructs a new ConfigurationException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}