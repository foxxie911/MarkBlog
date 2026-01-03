package dev.foxxie911.pages;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dev.foxxie911.models.Article;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ArticlePage {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final Logger LOGGER = Logger.getLogger("ArticlePage.class");
    private static final String MUSTACHE_PATH = "mustaches/article.mustache";

    private final List<Article> articleList;

    public ArticlePage(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void buildArticlePage() throws IOException {
        String blogName = DOTENV.get("BLOG_NAME");

        String htmlDir = DOTENV.get("SITE_PATH");

        if (htmlDir.startsWith("~")) {
            htmlDir = System.getProperty("user.home") + htmlDir.substring(1);
        }

        Path rootPath = Paths.get(htmlDir).toAbsolutePath();
        Path htmlPath = Files.createDirectories(rootPath.resolve("articles"));

        Mustache m = new DefaultMustacheFactory().compile(MUSTACHE_PATH);

        if (Files.notExists(htmlPath)) {
            try {
                Files.createDirectory(htmlPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        articleList.forEach(article -> {

            int year = article.createdAt().getYear();
            String month = article.createdAt().getMonth().toString();

            Map<String, Object> context = Map.of(
                    "blog_name", blogName,
                    "article", article
            );

            try {
                Path articlePagePath = Files.createDirectories(htmlPath.resolve(year + "/" + month));

                try (StringWriter writer = new StringWriter()) {
                    m.execute(writer, context).flush();
                    Files.writeString(
                            articlePagePath.resolve(article.title()
                                    .replaceAll("[\\p{Punct}\\s]", "") + ".html"),
                            writer.toString(),
                            StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING
                    );

                } catch (IOException e) {
                    LOGGER.severe("Couldn't compile the article: " + article);
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
