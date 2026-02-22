package dev.foxxie911.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.models.Article;

public class MarkdownParsingServiceTest {
    
    private MarkdownParsingService parsingService;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        parsingService = new MarkdownParsingService();
    }
    
    @Test
    void testValidArticleFile() throws IOException {
        Path validFile = tempDir.resolve("2023-12-01_my-article.md");
        Files.writeString(validFile, "# My Article\n\nThis is content.");
        
        assertTrue(parsingService.isValidArticleFile(validFile));
    }
    
    @Test
    void testInvalidArticleFile() throws IOException {
        Path invalidFile = tempDir.resolve("invalid-file.txt");
        Files.writeString(invalidFile, "Some content");
        
        assertFalse(parsingService.isValidArticleFile(invalidFile));
    }
    
    @Test
    void testParseValidArticle() throws IOException, FileProcessingException {
        Path articleFile = tempDir.resolve("2023-12-01_test-article.md");
        String markdownContent = "# Test Article\n\nThis is a **test** article.";
        Files.writeString(articleFile, markdownContent);
        
        Optional<Article> result = parsingService.parseArticle(articleFile);
        
        assertTrue(result.isPresent());
        Article article = result.get();
        assertEquals("test-article", article.title());
        assertEquals(LocalDate.of(2023, 12, 1), article.createdAt());
        assertTrue(article.body().contains("<h1>Test Article</h1>"));
        assertTrue(article.body().contains("<strong>test</strong>"));
    }
    
    @Test
    void testParseInvalidFileNameFormat() throws IOException, FileProcessingException {
        Path invalidFile = tempDir.resolve("invalid_name.md");
        Files.writeString(invalidFile, "# Content");
        
        Optional<Article> result = parsingService.parseArticle(invalidFile);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testParseInvalidDateInFileName() throws IOException, FileProcessingException {
        Path invalidFile = tempDir.resolve("2023-13-01_invalid-date.md");
        Files.writeString(invalidFile, "# Content");
        
        Optional<Article> result = parsingService.parseArticle(invalidFile);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testParseNonExistentFile() throws FileProcessingException {
        Path nonExistentFile = tempDir.resolve("non-existent.md");
        
        Optional<Article> result = parsingService.parseArticle(nonExistentFile);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testEmptyMarkdownContent() throws IOException, FileProcessingException {
        Path emptyFile = tempDir.resolve("2023-12-01_empty.md");
        Files.writeString(emptyFile, "");
        
        Optional<Article> result = parsingService.parseArticle(emptyFile);
        
        assertTrue(result.isPresent());
        Article article = result.get();
        assertEquals("", article.body());
    }
}
