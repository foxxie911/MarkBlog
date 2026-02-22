package dev.foxxie911.service;

import java.nio.file.Path;
import java.util.Optional;

import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.models.Article;

/**
 * Service interface for parsing article files.
 * Handles conversion of raw files into Article objects.
 */
public interface ArticleParsingService {
    
    /**
     * Parses an article file and creates an Article object.
     * 
     * @param filePath the path to the article file
     * @return Optional containing the parsed article, or empty if parsing failed
     * @throws FileProcessingException if there are issues reading or parsing the file
     */
    Optional<Article> parseArticle(Path filePath) throws FileProcessingException;
    
    /**
     * Validates if a file path represents a valid article file.
     * 
     * @param filePath the path to validate
     * @return true if the file is a valid article, false otherwise
     */
    boolean isValidArticleFile(Path filePath);
}
