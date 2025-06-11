package board.backend.article.infra;

import board.backend.article.domain.Article;

import java.time.Duration;
import java.util.Optional;

public interface ArticleCacheRepository {

    Optional<Article> get(Long articleId);

    void set(Article article, Duration ttl);

    void delete(Long articleId);

}
