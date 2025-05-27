package board.backend.application;

import board.backend.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleReader articleReader;
    private final ArticleWriter articleWriter;

    public List<Article> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        return articleReader.readAll(boardId, pageSize, lastArticleId);
    }

    public Article read(Long articleId) {
        return articleReader.read(articleId);
    }

    public Article create(Long boardId, Long writerId, String title, String content) {
        return articleWriter.create(boardId, writerId, title, content);
    }

    public Article update(Long articleId, String title, String content) {
        return articleWriter.update(articleId, title, content);
    }

    public void delete(Long articleId) {
        articleWriter.delete(articleId);
    }

}
