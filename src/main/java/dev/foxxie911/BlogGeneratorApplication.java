package dev.foxxie911;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.DefaultPicoContainer;
import dev.foxxie911.config.BlogConfiguration;
import dev.foxxie911.exception.ConfigurationException;
import dev.foxxie911.exception.FileProcessingException;
import dev.foxxie911.exception.MarkBlogException;
import dev.foxxie911.models.Article;
import dev.foxxie911.repository.FileSystemArticleRepository;
import dev.foxxie911.service.ArticleParsingService;
import dev.foxxie911.service.MarkdownParsingService;
import dev.foxxie911.service.TemplateRenderingService;
import dev.foxxie911.repository.ArticleRepository;
import dev.foxxie911.service.AssetManagementService;
import dev.foxxie911.service.PageGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Main application class that orchestrates the blog generation process.
 * Uses dependency injection to manage components and follows clean architecture principles.
 */
@Singleton
public class BlogGeneratorApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(BlogGeneratorApplication.class);
    
    private final BlogConfiguration configuration;
    private final ArticleRepository articleRepository;
    private final PageGenerationService pageGenerationService;
    private final AssetManagementService assetManagementService;
    
    @Inject
    public BlogGeneratorApplication(
            BlogConfiguration configuration,
            ArticleRepository articleRepository,
            PageGenerationService pageGenerationService,
            AssetManagementService assetManagementService) {
        
        this.configuration = configuration;
        this.articleRepository = articleRepository;
        this.pageGenerationService = pageGenerationService;
        this.assetManagementService = assetManagementService;
    }
    
    /**
     * Runs the complete blog generation process.
     * 
     * @throws MarkBlogException if any step of the process fails
     */
    public void run() throws MarkBlogException {
        logger.info("Starting MarkBlog generation process");
        logger.info("Configuration: {}", configuration);
        
        try {
            // Step 1: Load and validate articles
            List<Article> articles = loadArticles();
            
            if (articles.isEmpty()) {
                logger.warn("No articles found. Generation will create an empty blog.");
            } else {
                logger.info("Loaded {} articles successfully", articles.size());
            }
            
            // Step 2: Generate HTML pages
            generatePages(articles);
            
            // Step 3: Copy static assets
            copyAssets();
            
            logger.info("MarkBlog generation completed successfully!");
            
        } catch (Exception e) {
            logger.error("Blog generation failed", e);
            if (e instanceof MarkBlogException) {
                throw e;
            } else {
                throw new MarkBlogException("Unexpected error during blog generation", e);
            }
        }
    }
    
    /**
     * Loads all articles from the configured source.
     * 
     * @return list of loaded articles
     * @throws FileProcessingException if there are issues loading articles
     */
    private List<Article> loadArticles() throws FileProcessingException {
        logger.info("Loading articles...");
        return articleRepository.findAll();
    }
    
    /**
     * Generates all HTML pages from the loaded articles.
     * 
     * @param articles the articles to generate pages for
     * @throws FileProcessingException if there are issues generating pages
     */
    private void generatePages(List<Article> articles) throws FileProcessingException {
        logger.info("Generating pages...");
        pageGenerationService.generateHomePage(articles);
        pageGenerationService.generateArticlePages(articles);
    }
    
    /**
     * Copies all static assets to the output directory.
     * 
     * @throws FileProcessingException if there are issues copying assets
     */
    private void copyAssets() throws FileProcessingException {
        logger.info("Copying static assets...");
        assetManagementService.copyAllAssets();
    }
    
    /**
     * Main entry point for the application.
     * Sets up dependency injection and runs the blog generation process.
     */
    public static void main() {
        try {
            // Set up dependency injection
            MutablePicoContainer container = new DefaultPicoContainer();
            
            // Register components
            container.addComponent(BlogConfiguration.class);
            container.addComponent(ArticleRepository.class, FileSystemArticleRepository.class);
            container.addComponent(ArticleParsingService.class, MarkdownParsingService.class);
            container.addComponent(TemplateRenderingService.class);
            container.addComponent(AssetManagementService.class);
            container.addComponent(PageGenerationService.class);
            container.addComponent(MarkdownParsingService.class);
            container.addComponent(FileSystemArticleRepository.class);
            
            // Get the main application instance
            // Manually instantiate since PicoContainer doesn't auto-wire the main class
            BlogGeneratorApplication application = new BlogGeneratorApplication(
                container.getComponent(BlogConfiguration.class),
                container.getComponent(ArticleRepository.class),
                container.getComponent(PageGenerationService.class),
                container.getComponent(AssetManagementService.class)
            );
            
            // Run the application
            application.run();
            
        } catch (ConfigurationException e) {
            logger.error("Configuration error: {}", e.getMessage());
            System.exit(1);
        } catch (MarkBlogException e) {
            logger.error("Application error: {}", e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            System.exit(1);
        }
    }
}