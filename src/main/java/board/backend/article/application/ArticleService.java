package board.backend.article.application;

import board.backend.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleReader articleReader;
    private final ArticleWriter articleWriter;

    public Article read(Long articleId, String ip) {
        return articleReader.read(articleId, ip);
    }

    public Article create(Long boardId, Long userId, String title, String content) {
        return articleWriter.create(boardId, userId, title, content);
    }

    public Article update(Long articleId, Long userId, String title, String content) {
        return articleWriter.update(articleId, userId, title, content);
    }

    public void delete(Long articleId, Long userId) {
        articleWriter.delete(articleId, userId);
    }

}
