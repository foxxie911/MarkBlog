package dev.foxxie911.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration class that encapsulates all blog settings and environment variables.
 * Provides type-safe access to configuration values with proper validation.
 */
public class BlogConfiguration {
    
    private final Dotenv dotenv;
    private final String blogName;
    private final String blogBio;
    private final Path sitePath;
    private final Path articlePath;
    
    /**
     * Constructs a new BlogConfiguration instance.
     * Loads environment variables and validates required configuration.
     * 
     * @throws IllegalStateException if required configuration is missing or invalid
     */
    public BlogConfiguration() {
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.blogName = loadRequiredProperty("BLOG_NAME");
        this.blogBio = loadRequiredProperty("BLOG_BIO");
        this.sitePath = resolvePath(loadRequiredProperty("SITE_PATH"));
        this.articlePath = resolvePath(loadRequiredProperty("ARTICLE_PATH"));
    }
    
    /**
     * Gets the blog name from configuration.
     * 
     * @return the blog name
     */
    public String getBlogName() {
        return blogName;
    }
    
    /**
     * Gets the blog bio/description from configuration.
     * 
     * @return the blog bio
     */
    public String getBlogBio() {
        return blogBio;
    }
    
    /**
     * Gets the site output path.
     * 
     * @return the resolved site path
     */
    public Path getSitePath() {
        return sitePath;
    }
    
    /**
     * Gets the article source path.
     * 
     * @return the resolved article path
     */
    public Path getArticlePath() {
        return articlePath;
    }
    
    /**
     * Loads a required property from environment variables.
     * 
     * @param propertyName the name of the property to load
     * @return the property value
     * @throws IllegalStateException if the property is missing or empty
     */
    private String loadRequiredProperty(String propertyName) {
        String value = dotenv.get(propertyName);
        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException(
                String.format("Required configuration property '%s' is missing or empty", propertyName)
            );
        }
        return value.trim();
    }
    
    /**
     * Resolves a path string, handling tilde expansion for home directory.
     * 
     * @param pathString the path string to resolve
     * @return the resolved Path object
     */
    private Path resolvePath(String pathString) {
        String resolvedPath = pathString;
        if (pathString.startsWith("~")) {
            resolvedPath = System.getProperty("user.home") + pathString.substring(1);
        }
        return Paths.get(resolvedPath).toAbsolutePath().normalize();
    }
    
    @Override
    public String toString() {
        return String.format(
            "BlogConfiguration{blogName='%s', blogBio='%s', sitePath=%s, articlePath=%s}",
            blogName, blogBio, sitePath, articlePath
        );
    }
}
