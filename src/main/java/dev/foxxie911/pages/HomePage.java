package dev.foxxie911.pages;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dev.foxxie911.models.Article;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HomePage {

    private static final Logger LOGGER = Logger.getLogger("HomePage.class");
    private static final Path HTML_PATH = Path.of("./public");
    private static final String MUSTACHE_PATH = "mustaches/home.mustache";
    private static final String BLOG_NAME = "Foxxie's Den";
    private static final String BLOG_BIO = "Here I write about my analysis, understanding and feelings as well. " +
            "That could be related to technology, Islam, philosophy, economics or anything else";
    private final List<Article> articleList;


    public HomePage(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void buildHomePage() {
        Mustache m = new DefaultMustacheFactory().compile(MUSTACHE_PATH);

        if(Files.notExists(HTML_PATH)){
            try {
                Files.createDirectory(HTML_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> articleTitles = articleList.stream()
                .map(Article::title)
                .toList();

        Map<String, Object> context = Map.of(
                "blog_name", BLOG_NAME,
                "blog_bio", BLOG_BIO,
                "article_list", articleTitles
        );

        try (StringWriter writer = new StringWriter()) {
            m.execute(writer, context).flush();
            Files.writeString(
                    HTML_PATH.resolve("home.html"),
                    writer.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            LOGGER.severe("Failed to generate: home.html");
            throw new UncheckedIOException(e);
        }
    }

}
