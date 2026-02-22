package dev.foxxie911.repository;

import java.util.List;

import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.models.Article;

/**
 * Repository interface for article data access operations.
 * Provides methods for retrieving and managing article data.
 */
public interface ArticleRepository {
    
    /**
     * Retrieves all articles from the configured source.
     * 
     * @return a list of all articles, ordered by creation date (newest first)
     * @throws FileProcessingException if there are issues accessing or processing article files
     */
    List<Article> findAll() throws FileProcessingException;
    
    /**
     * Checks if any articles exist in the configured source.
     * 
     * @return true if articles exist, false otherwise
     * @throws FileProcessingException if there are issues accessing the article source
     */
    boolean hasArticles() throws FileProcessingException;
}
