package board.backend.article.infra;

import board.backend.article.domain.Article;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
class ArticleCacheRepositoryImpl implements ArticleCacheRepository {

    private static final String KEY_FORMAT = "article::%s";

    private final RedisTemplate<String, Object> articles;

    ArticleCacheRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.articles = redisTemplate;
    }

    @Override
    public Optional<Article> get(Long articleId) {
        Object value = articles.opsForValue().get(generateArticleKey(articleId));
        if (value instanceof Article article) {
            return Optional.of(article);
        }
        return Optional.empty();
    }

    @Override
    public void set(Article article, Duration ttl) {
        articles.opsForValue().set(generateArticleKey(article.getId()), article, ttl);
    }

    @Override
    public void delete(Long articleId) {
        articles.delete(generateArticleKey(articleId));
    }

    private String generateArticleKey(Long articleId) {
        return String.format(KEY_FORMAT, articleId);
    }

}
