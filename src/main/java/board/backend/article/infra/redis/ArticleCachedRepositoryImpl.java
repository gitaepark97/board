package board.backend.article.infra.redis;

import board.backend.article.domain.Article;
import board.backend.common.infra.AbstractRedisCachedRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleCachedRepositoryImpl extends AbstractRedisCachedRepository<Article, Long> {

    ArticleCachedRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "article::%s", Article.class);
    }

}
