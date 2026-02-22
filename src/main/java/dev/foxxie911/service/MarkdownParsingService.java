package dev.foxxie911.service;

import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.models.Article;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Service implementation for parsing Markdown article files.
 * Handles file reading, metadata extraction, and Markdown to HTML conversion.
 */
@Singleton
public class MarkdownParsingService implements ArticleParsingService {
    
    private static final Logger logger = LoggerFactory.getLogger(MarkdownParsingService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;
    
    /**
     * Constructs a new MarkdownParsingService with default parser and renderer.
     */
    public MarkdownParsingService() {
        this.markdownParser = Parser.builder().build();
        this.htmlRenderer = HtmlRenderer.builder().build();
    }
    
    /**
     * Constructs a new MarkdownParsingService with custom parser and renderer.
     * 
     * @param markdownParser the Markdown parser to use
     * @param htmlRenderer the HTML renderer to use
     */
    public MarkdownParsingService(Parser markdownParser, HtmlRenderer htmlRenderer) {
        this.markdownParser = markdownParser;
        this.htmlRenderer = htmlRenderer;
    }
    
    @Override
    public Optional<Article> parseArticle(Path filePath) throws FileProcessingException {
        if (!isValidArticleFile(filePath)) {
            logger.debug("Skipping invalid article file: {}", filePath.getFileName());
            return Optional.empty();
        }
        
        try {
            String fileName = filePath.getFileName().toString();
            String[] fileNameParts = fileName.split("_", 2);
            
            if (fileNameParts.length < 2) {
                logger.warn("Invalid filename format for article: {}", fileName);
                return Optional.empty();
            }
            
            LocalDate createdAt = parseDateFromFileName(fileNameParts[0]);
            String title = extractTitleFromFileName(fileNameParts[1]);
            String body = convertMarkdownToHtml(filePath);
            
            return Optional.of(new Article(title, createdAt, body));
            
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date from filename: {}", filePath.getFileName(), e);
            return Optional.empty();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to read article file", e, filePath);
        } catch (Exception e) {
            logger.error("Unexpected error parsing article: {}", filePath.getFileName(), e);
            return Optional.empty();
        }
    }
    
    @Override
    public boolean isValidArticleFile(Path filePath) {
        if (filePath == null || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            return false;
        }
        
        String fileName = filePath.getFileName().toString();
        return fileName.matches("\\d{4}-\\d{2}-\\d{2}_[^_]+\\.md");
    }
    
    /**
     * Parses the date portion from a filename.
     * 
     * @param dateString the date string in yyyy-MM-dd format
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the date string is invalid
     */
    private LocalDate parseDateFromFileName(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * Extracts the title from the filename portion.
     * 
     * @param fileNamePart the filename part containing the title
     * @return the extracted title
     */
    private String extractTitleFromFileName(String fileNamePart) {
        int extensionIndex = fileNamePart.lastIndexOf('.');
        if (extensionIndex > 0) {
            return fileNamePart.substring(0, extensionIndex);
        }
        return fileNamePart;
    }
    
    /**
     * Converts Markdown content to HTML.
     * 
     * @param filePath the path to the Markdown file
     * @return the HTML representation of the content
     * @throws IOException if there are issues reading the file
     */
    private String convertMarkdownToHtml(Path filePath) throws IOException {
        String markdownContent = Files.readString(filePath, StandardCharsets.UTF_8);
        
        if (StringUtils.isBlank(markdownContent)) {
            logger.warn("Empty markdown content in file: {}", filePath.getFileName());
            return "";
        }
        
        Node document = markdownParser.parse(markdownContent);
        return htmlRenderer.render(document);
    }
}