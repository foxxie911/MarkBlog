package dev.foxxie911.services;

import dev.foxxie911.models.Article;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ArticleLister {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final Logger logger = Logger.getLogger("ArticleLister.class");

    public ArticleLister() {
    }

    public List<Article> listArticle() throws IOException{

        String articleDir = DOTENV.get("ARTICLE_PATH");
        if (articleDir.startsWith("~")) {
            articleDir = System.getProperty("user.home") + articleDir.substring(1);
        }

        Path articlePath = Paths.get(articleDir).toAbsolutePath();

        if (Files.notExists(articlePath)) {
            logger.warning("No files found in the \"" + articlePath + "\"");
            return List.of();
        }


        try (var path = Files.walk(articlePath)) {
                return path.filter(Files::isRegularFile)
                    .map(ArticleBuilder::new)
                    .map(ArticleBuilder::prepareArticle)
                    .flatMap(Optional::stream)
                    .toList();
        }
    }
}
