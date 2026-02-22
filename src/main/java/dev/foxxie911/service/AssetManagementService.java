package dev.foxxie911.service;

import dev.foxxie911.config.BlogConfiguration;
import dev.foxxie911.exception.FileProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import javax.annotation.Nonnull;

/**
 * Service for managing static assets like CSS files and fonts.
 * Handles copying and organizing asset files for the generated blog.
 */
@Singleton
public class AssetManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(AssetManagementService.class);
    
    private final BlogConfiguration configuration;
    
    @Inject
    public AssetManagementService(BlogConfiguration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * Copies all stylesheet files to the output directory.
     * 
     * @throws FileProcessingException if there are issues copying stylesheet files
     */
    public void copyStylesheets() throws FileProcessingException {
        Path sourceStylesPath = Paths.get("src/main/resources/styles").toAbsolutePath();
        Path targetStylesPath = configuration.getSitePath().resolve("styles");
        
        logger.info("Copying stylesheets from {} to {}", sourceStylesPath, targetStylesPath);
        
        if (!Files.exists(sourceStylesPath)) {
            logger.warn("Stylesheet directory does not exist: {}", sourceStylesPath);
            return;
        }
        
        try {
            Files.createDirectories(targetStylesPath);
            Files.walk(sourceStylesPath)
                .filter(Files::isRegularFile)
                .forEach(sourceFile -> {
                    try {
                        Path relativePath = sourceStylesPath.relativize(sourceFile);
                        Path targetFile = targetStylesPath.resolve(relativePath);
                        Files.createDirectories(targetFile.getParent());
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        logger.debug("Copied stylesheet: {}", relativePath);
                    } catch (IOException e) {
                        logger.error("Failed to copy stylesheet: {}", sourceFile.getFileName(), e);
                    }
                });
                
            logger.info("Successfully copied stylesheet files");
        } catch (IOException e) {
            throw new FileProcessingException("Failed to copy stylesheet files");
        }
    }
    
    /**
     * Copies all font files to the output directory.
     * 
     * @throws FileProcessingException if there are issues copying font files
     */
    public void copyFonts() throws FileProcessingException {
        Path sourceFontsPath = Paths.get("src/main/resources/fonts").toAbsolutePath();
        Path targetFontsPath = configuration.getSitePath().resolve("fonts");
        
        logger.info("Copying fonts from {} to {}", sourceFontsPath, targetFontsPath);
        
        if (!Files.exists(sourceFontsPath)) {
            logger.warn("Fonts directory does not exist: {}", sourceFontsPath);
            return;
        }
        
        try {
            Files.createDirectories(targetFontsPath);
            Files.walkFileTree(sourceFontsPath, new SimpleFileVisitor<Path>() {
                @Override
                public @Nonnull FileVisitResult visitFile(@Nonnull Path file, @Nonnull BasicFileAttributes attrs) throws IOException {
                    Path relativePath = sourceFontsPath.relativize(file);
                    Path targetFile = targetFontsPath.resolve(relativePath);
                    Files.createDirectories(targetFile.getParent());
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    logger.debug("Copied font: {}", relativePath);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    logger.error("Failed to copy font file: {}", file.getFileName(), exc);
                    return FileVisitResult.CONTINUE;
                }
            });
            
            logger.info("Successfully copied font files");
        } catch (IOException e) {
            throw new FileProcessingException("Failed to copy font files");
        }
    }
    
    /**
     * Copies all static assets (stylesheets and fonts) to the output directory.
     * 
     * @throws FileProcessingException if there are issues copying any assets
     */
    public void copyAllAssets() throws FileProcessingException {
        copyStylesheets();
        copyFonts();
    }
}