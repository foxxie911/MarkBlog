package dev.foxxie911;

import dev.foxxie911.models.Article;
import dev.foxxie911.pages.ArticlePage;
import dev.foxxie911.pages.HomePage;
import dev.foxxie911.services.ArticleLister;

import java.io.IOException;
import java.util.List;

public class Main {
    static void main() throws IOException {
        // TODO make a builder so that main method contains only one method call
        ArticleLister lister = new ArticleLister();
        List<Article> list = lister.listArticle();

        new HomePage(list).buildHomePage();
        new ArticlePage(list).buildArticlePage();

    }
}
