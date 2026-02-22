package dev.foxxie911.exception;

import java.nio.file.Path;

/**
 * Exception thrown when there are issues processing files.
 * Used for file I/O operations, parsing failures, or file validation errors.
 */
public class FileProcessingException extends MarkBlogException {
    
    private final Path filePath;
    
    /**
     * Constructs a new FileProcessingException with the specified detail message.
     * 
     * @param message the detail message
     */
    public FileProcessingException(String message) {
        super(message);
        this.filePath = null;
    }
    
    /**
     * Constructs a new FileProcessingException with the specified detail message and file path.
     * 
     * @param message the detail message
     * @param filePath the path of the file that caused the exception
     */
    public FileProcessingException(String message, Path filePath) {
        super(message + " File: " + filePath);
        this.filePath = filePath;
    }
    
    /**
     * Constructs a new FileProcessingException with the specified detail message, cause, and file path.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     * @param filePath the path of the file that caused the exception
     */
    public FileProcessingException(String message, Throwable cause, Path filePath) {
        super(message + " File: " + filePath, cause);
        this.filePath = filePath;
    }
    
    /**
     * Gets the file path that caused this exception, if available.
     * 
     * @return the file path, or null if not available
     */
    public Path getFilePath() {
        return filePath;
    }
}