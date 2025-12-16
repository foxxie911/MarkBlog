package dev.foxxie911.services;

import dev.foxxie911.models.Article;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

public class ArticleBuilder{

    private final Path filePath;

    private static final Logger logger = Logger.getLogger("ArticleBuilder.class");

    public ArticleBuilder(Path filePath) {
        this.filePath = filePath;
    }

    public Optional<Article> prepareArticle(){

        if(!Files.exists(filePath)){
            logger.warning("File with the name \"" + filePath + "\"couldn't be found!");
            return Optional.empty();
        }

        String fileName = String.valueOf(filePath.getFileName());
        String[] dateNTitle = fileName.split("_");

        String title = dateNTitle[1];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate createdAt = LocalDate.parse(dateNTitle[0], formatter);

        String body = markToHtml();

        return Optional.of(new Article(title, createdAt, body));
    }

    private String markToHtml(){
        try {
            String markString = Files.readString(filePath, StandardCharsets.UTF_8);
            Parser parser = Parser.builder().build();
            Node parsedString = parser.parse(markString);
            HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
            return htmlRenderer.render(parsedString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
