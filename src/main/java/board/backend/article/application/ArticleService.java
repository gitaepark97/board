package board.backend.article.application;

import board.backend.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final CacheManager cacheManager;
    private final ArticleReader articleReader;
    private final ArticleWriter articleWriter;

    public Article read(Long articleId, String ip) {
        return articleReader.read(articleId, ip);
    }

    @CacheEvict(value = "articleList::board", key = "#boardId")
    public Article create(Long boardId, Long userId, String title, String content) {
        return articleWriter.create(boardId, userId, title, content);
    }

    public Article update(Long articleId, Long userId, String title, String content) {
        return articleWriter.update(articleId, userId, title, content);
    }

    public void delete(Long articleId, Long userId) {
        Optional<Long> boardId = articleWriter.delete(articleId, userId);
        boardId.ifPresent(id -> {
            Cache cache = cacheManager.getCache("articleList::board");
            if (cache != null) {
                cache.evict(id);
            }
        });
    }

}
