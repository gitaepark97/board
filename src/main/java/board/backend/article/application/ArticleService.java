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
    private final ArticleCreator articleCreator;
    private final ArticleUpdater articleUpdater;
    private final ArticleDeleter articleDeleter;

    @CacheEvict(
        value = "article::list::board",
        key = "#boardId"
    )
    public Article create(Long boardId, Long userId, String title, String content) {
        return articleCreator.create(boardId, userId, title, content);
    }

    public Article update(Long articleId, Long userId, String title, String content) {
        return articleUpdater.update(articleId, userId, title, content);
    }

    public void delete(Long articleId, Long userId) {
        Optional<Long> boardId = articleDeleter.delete(articleId, userId);
        boardId.ifPresent(id -> {
            Cache cache = cacheManager.getCache("article::list::board");
            if (cache != null) {
                cache.evict(id);
            }
        });
    }

}
