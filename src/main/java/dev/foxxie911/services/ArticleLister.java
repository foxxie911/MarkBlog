package dev.foxxie911.services;

import dev.foxxie911.models.Article;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ArticleLister {
    private static final Path ARTICLE_DIRECTORY = Path.of("./articles");
    private static final Logger logger = Logger.getLogger("ArticleLister.class");

    public ArticleLister() {
    }

    public List<Article> listArticle() throws IOException{

        if (Files.notExists(ARTICLE_DIRECTORY)) {
            logger.warning("No files found in the \"" + ARTICLE_DIRECTORY + "\"");
            return List.of();
        }


        try(var path = Files.walk(ARTICLE_DIRECTORY)) {
                return path.filter(Files::isRegularFile)
                    .map(ArticleBuilder::new)
                    .map(ArticleBuilder::prepareArticle)
                    .flatMap(Optional::stream)
                    .toList();
        }
    }
}
