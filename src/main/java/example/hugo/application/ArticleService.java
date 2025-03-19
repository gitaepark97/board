package example.hugo.application;

import example.hugo.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleReader articleReader;
    private final ArticleWriter articleWriter;

    public Article readArticle(Long articleId) {
        return articleReader.readArticle(articleId);
    }

    public List<Article> readArticles(Long boardId, Long pageSize, Long lastArticleId) {
        return articleReader.readArticles(boardId, pageSize, lastArticleId);
    }

    public Long countBoardArticles(Long boardId) {
        return articleReader.countBoardArticles(boardId);
    }

    public Long countArticleViews(Long articleId) {
        return articleReader.countArticleViews(articleId);
    }

    public Article createArticle(String title, String content, Long boardId, Long writerId) {
        return articleWriter.createArticle(title, content, boardId, writerId);
    }

    public Article updateArticle(Long articleId, String title, String content) {
        return articleWriter.updateArticle(articleId, title, content);
    }

    public void deleteArticle(Long articleId) {
        articleWriter.deleteArticle(articleId);
    }

}
