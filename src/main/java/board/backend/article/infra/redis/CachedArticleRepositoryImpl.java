package board.backend.article.infra.redis;

import board.backend.article.domain.Article;
import board.backend.common.cache.infra.AbstractRedisCachedRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class CachedArticleRepositoryImpl extends AbstractRedisCachedRepository<Article, Long> {

    CachedArticleRepositoryImpl(
        @Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate,
        ObjectMapper objectMapper
    ) {
        super(redisTemplate, "article::%s", Article.class, objectMapper);
    }

}
