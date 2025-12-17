package dev.foxxie911.pages;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dev.foxxie911.models.Article;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ArticlePage {
    private static final Logger LOGGER = Logger.getLogger("ArticlePage.class");
    private static final Path HTML_PATH = Path.of("./public/articles");
    private static final String MUSTACHE_PATH = "mustaches/article.mustache";

    private final List<Article> articleList;

    public ArticlePage(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void buildArticlePage(){
        Mustache m = new DefaultMustacheFactory().compile(MUSTACHE_PATH);

        if(Files.notExists(HTML_PATH)){
            try {
                Files.createDirectory(HTML_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        articleList.forEach(article -> {
                    Map<String, Object> context = Map.of(
                            "article", article
                    );
                    try(StringWriter writer = new StringWriter()){
                        m.execute(writer, context).flush();
                        Files.writeString(
                                HTML_PATH.resolve(article.title() + ".html"),
                                writer.toString(),
                                StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING
                        );

                    } catch (IOException e) {
                        LOGGER.severe("Couldn't compile the article: " + article);
                        throw new RuntimeException(e);
                    }
                });
    }
}
