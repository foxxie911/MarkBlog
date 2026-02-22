package dev.foxxie911.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import dev.foxxie911.exception.FileProcessingException;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Service for rendering templates using Mustache.
 * Handles template compilation and rendering with context data.
 */
@Singleton
public class TemplateRenderingService {
    
    private final MustacheFactory mustacheFactory;
    
    /**
     * Constructs a new TemplateRenderingService with default Mustache factory.
     */
    public TemplateRenderingService() {
        this.mustacheFactory = new DefaultMustacheFactory();
    }
    
    /**
     * Constructs a new TemplateRenderingService with custom Mustache factory.
     * 
     * @param mustacheFactory the Mustache factory to use
     */
    public TemplateRenderingService(MustacheFactory mustacheFactory) {
        this.mustacheFactory = mustacheFactory;
    }
    
    /**
     * Renders a template with the given context data.
     * 
     * @param templatePath the path to the template file
     * @param context the context data for template rendering
     * @return the rendered template as a string
     * @throws FileProcessingException if there are issues compiling or rendering the template
     */
    public String renderTemplate(String templatePath, Map<String, Object> context) throws FileProcessingException {
        try {
            Mustache mustache = mustacheFactory.compile(templatePath);
            Writer writer = new StringWriter();
            mustache.execute(writer, context);
            writer.flush();
            return writer.toString();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to render template: " + templatePath);
        }
    }
}