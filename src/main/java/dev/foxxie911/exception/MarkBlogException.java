package dev.foxxie911.exception;

/**
 * Base exception class for all MarkBlog application exceptions.
 * Provides a foundation for consistent error handling throughout the application.
 */
public class MarkBlogException extends Exception {
    
    /**
     * Constructs a new MarkBlogException with the specified detail message.
     * 
     * @param message the detail message
     */
    public MarkBlogException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new MarkBlogException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public MarkBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}
