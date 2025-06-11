package board.backend.article.infra;

import board.backend.article.domain.Article;
import board.backend.common.infra.AbstractRedisCacheRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleCacheRepositoryImpl extends AbstractRedisCacheRepository<Article, Long> {

    ArticleCacheRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "article::%s", Article.class);
    }

}
