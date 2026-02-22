package dev.foxxie911.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.foxxie911.config.BlogConfiguration;
import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.models.Article;
import dev.foxxie911.models.ArticleList;

/**
 * Service for generating HTML pages from templates and article data.
 * Handles both homepage and individual article page generation.
 */
@Singleton
public class PageGenerationService {
    
    private static final Logger logger = LoggerFactory.getLogger(PageGenerationService.class);
    private static final String HOME_TEMPLATE = "mustaches/home.mustache";
    private static final String ARTICLE_TEMPLATE = "mustaches/article.mustache";
    
    private final BlogConfiguration configuration;
    private final TemplateRenderingService templateService;
    
    @Inject
    public PageGenerationService(BlogConfiguration configuration, TemplateRenderingService templateService) {
        this.configuration = configuration;
        this.templateService = templateService;
    }
    
    /**
     * Generates the homepage with a list of all articles.
     * 
     * @param articles the list of articles to display
     * @throws FileProcessingException if there are issues generating the homepage
     */
    public void generateHomePage(List<Article> articles) throws FileProcessingException {
        logger.info("Generating homepage with {} articles", articles.size());
        
        List<ArticleList> articleListItems = articles.stream()
            .map(this::createArticleListItem)
            .collect(Collectors.toList());
        
        Map<String, Object> context = new HashMap<>();
        context.put("blog_name", configuration.getBlogName());
        context.put("blog_bio", configuration.getBlogBio());
        context.put("article_list", articleListItems);
        
        String renderedHtml = templateService.renderTemplate(HOME_TEMPLATE, context);
        
        Path outputPath = configuration.getSitePath().resolve("index.html");
        writeHtmlFile(outputPath, renderedHtml);
        
        logger.info("Homepage generated successfully at: {}", outputPath);
    }
    
    /**
     * Generates individual article pages for all articles.
     * 
     * @param articles the list of articles to generate pages for
     * @throws FileProcessingException if there are issues generating article pages
     */
    public void generateArticlePages(List<Article> articles) throws FileProcessingException {
        logger.info("Generating {} article pages", articles.size());
        
        Path articlesBasePath = configuration.getSitePath().resolve("articles");
        
        for (Article article : articles) {
            generateSingleArticlePage(article, articlesBasePath);
        }
        
        logger.info("All article pages generated successfully");
    }
    
    /**
     * Generates a single article page.
     * 
     * @param article the article to generate a page for
     * @param articlesBasePath the base path for article pages
     * @throws FileProcessingException if there are issues generating the page
     */
    private void generateSingleArticlePage(Article article, Path articlesBasePath) throws FileProcessingException {
        String year = String.valueOf(article.createdAt().getYear());
        String month = article.createdAt().getMonth().toString();
        
        Path articlePath = articlesBasePath.resolve(year).resolve(month);
        
        Map<String, Object> context = new HashMap<>();
        context.put("blog_name", configuration.getBlogName());
        context.put("article", article);
        
        String renderedHtml = templateService.renderTemplate(ARTICLE_TEMPLATE, context);
        
        String fileName = article.title().replaceAll("[\\p{Punct}\\s]", "") + ".html";
        Path outputPath = articlePath.resolve(fileName);
        
        writeHtmlFile(outputPath, renderedHtml);
        logger.debug("Generated article page: {}", outputPath);
    }
    
    /**
     * Creates an ArticleList item from an Article for homepage display.
     * 
     * @param article the article to convert
     * @return the ArticleList item
     */
    private ArticleList createArticleListItem(Article article) {
        String title = article.title();
        int year = article.createdAt().getYear();
        String month = article.createdAt().getMonth().toString();
        String link = year + "/" + month + "/" + title.replaceAll("[\\p{Punct}\\s]", "") + ".html";
        return new ArticleList(title, link);
    }
    
    /**
     * Writes HTML content to a file, creating directories as needed.
     * 
     * @param outputPath the path to write the HTML file to
     * @param htmlContent the HTML content to write
     * @throws FileProcessingException if there are issues writing the file
     */
    private void writeHtmlFile(Path outputPath, String htmlContent) throws FileProcessingException {
        try {
            Files.createDirectories(outputPath.getParent());
            Files.writeString(
                outputPath,
                htmlContent,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new FileProcessingException("Failed to write HTML file: " + outputPath, e, outputPath);
        }
    }
}
