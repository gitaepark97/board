package board.backend.like.infra.redis;

import board.backend.common.cache.infra.AbstractRedisCachedRepository;
import board.backend.like.domain.ArticleLikeCount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class CachedArticleLikeCountRepository extends AbstractRedisCachedRepository<ArticleLikeCount, Long> {

    protected CachedArticleLikeCountRepository(
        @Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate,
        ObjectMapper objectMapper
    ) {
        super(redisTemplate, "like::article::%s", ArticleLikeCount.class, objectMapper);
    }

}
