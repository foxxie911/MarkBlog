package dev.foxxie911.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.foxxie911.config.BlogConfiguration;
import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.models.Article;
import dev.foxxie911.service.ArticleParsingService;

/**
 * File system implementation of ArticleRepository.
 * Handles reading and parsing articles from the file system.
 */
@Singleton
public class FileSystemArticleRepository implements ArticleRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(FileSystemArticleRepository.class);
    
    private final BlogConfiguration configuration;
    private final ArticleParsingService parsingService;
    
    /**
     * Constructs a new FileSystemArticleRepository.
     * 
     * @param configuration the blog configuration
     * @param parsingService the service for parsing article files
     */
    @Inject
    public FileSystemArticleRepository(BlogConfiguration configuration, ArticleParsingService parsingService) {
        this.configuration = configuration;
        this.parsingService = parsingService;
    }
    
    @Override
    public List<Article> findAll() throws FileProcessingException {
        Path articlePath = configuration.getArticlePath();
        
        logger.info("Searching for articles in: {}", articlePath);
        
        if (!Files.exists(articlePath)) {
            logger.warn("Article directory does not exist: {}", articlePath);
            return Collections.emptyList();
        }
        
        if (!Files.isDirectory(articlePath)) {
            throw new FileProcessingException("Configured article path is not a directory: " + articlePath, articlePath);
        }
        
        try (var stream = Files.walk(articlePath)) {
            List<Article> articles = stream
                .filter(Files::isRegularFile)
                .map(this::parseArticleFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted((a1, a2) -> a2.createdAt().compareTo(a1.createdAt())) // Newest first
                .collect(Collectors.toList());
            
            logger.info("Found {} articles", articles.size());
            return articles;
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to read article directory: " + articlePath, e, articlePath);
        }
    }
    
    @Override
    public boolean hasArticles() throws FileProcessingException {
        Path articlePath = configuration.getArticlePath();
        
        if (!Files.exists(articlePath) || !Files.isDirectory(articlePath)) {
            return false;
        }
        
        try (var stream = Files.walk(articlePath)) {
            return stream.anyMatch(Files::isRegularFile);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to check for articles in: " + articlePath, e, articlePath);
        }
    }
    
    /**
     * Parses a single article file.
     * 
     * @param filePath the path to the article file
     * @return Optional containing the parsed article, or empty if parsing failed
     */
    private Optional<Article> parseArticleFile(Path filePath) {
        try {
            logger.debug("Parsing article file: {}", filePath.getFileName());
            return parsingService.parseArticle(filePath);
        } catch (Exception e) {
            logger.error("Failed to parse article file: {}", filePath.getFileName(), e);
            return Optional.empty();
        }
    }
}
