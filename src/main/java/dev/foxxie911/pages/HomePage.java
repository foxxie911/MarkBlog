package dev.foxxie911.pages;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dev.foxxie911.models.Article;
import dev.foxxie911.models.ArticleList;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HomePage {

    private static final Dotenv DOTENV = Dotenv.load();
    private static final Logger LOGGER = Logger.getLogger("HomePage.class");
    private static final String MUSTACHE_PATH = "mustaches/home.mustache";
    private final List<Article> articleList;


    public HomePage(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void buildHomePage() {
        String blogName = DOTENV.get("BLOG_NAME");
        String blogBio = DOTENV.get("BLOG_BIO");

        String dir = DOTENV.get("SITE_PATH");

        if (dir.startsWith("~")) {
            dir = System.getProperty("user.home") + dir.substring(1);
        }

        Path htmlPath = Paths.get(dir).toAbsolutePath();

        Mustache m = new DefaultMustacheFactory().compile(MUSTACHE_PATH);

        if (Files.notExists(htmlPath)) {
            try {
                Files.createDirectory(htmlPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<ArticleList> articleInfo = articleList.stream()
                .map(article -> {
                    String title = article.title();
                    int year = article.createdAt().getYear();
                    String month = article.createdAt().getMonth().toString();
                    String link =  year + "/" + month + "/" + title + ".html";
                    return new ArticleList(title, link);
                })
                .toList()
                .reversed();

        Map<String, Object> context = Map.of(
                "blog_name", blogName,
                "blog_bio", blogBio,
                "article_list", articleInfo
        );

        try (StringWriter writer = new StringWriter()) {
            m.execute(writer, context).flush();
            Files.writeString(
                    htmlPath.resolve("index.html"),
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