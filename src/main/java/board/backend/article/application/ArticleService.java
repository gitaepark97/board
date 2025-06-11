package board.backend.article.application;

import board.backend.article.application.dto.ArticleWithWriterAndCounts;
import board.backend.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleReader articleReader;
    private final ArticleWriter articleWriter;

    @Cacheable(value = "articleList::board", key = "#boardId", condition = "#pageSize.equals(10L) && #lastArticleId == null")
    public List<ArticleWithWriterAndCounts> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        return articleReader.readAll(boardId, pageSize, lastArticleId);
    }

    public Article read(Long articleId, String ip) {
        return articleReader.read(articleId, ip);
    }

    public Article create(Long boardId, Long writerId, String title, String content) {
        return articleWriter.create(boardId, writerId, title, content);
    }

    public Article update(Long articleId, Long userId, String title, String content) {
        return articleWriter.update(articleId, userId, title, content);
    }

    public void delete(Long articleId, Long userId) {
        articleWriter.delete(articleId, userId);
    }

}
