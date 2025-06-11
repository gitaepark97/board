package board.backend.like.infra;

import board.backend.common.infra.AbstractRedisCacheRepository;
import board.backend.like.domain.ArticleLikeCount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleLikeCountCacheRepository extends AbstractRedisCacheRepository<ArticleLikeCount, Long> {

    protected ArticleLikeCountCacheRepository(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "like::article::%s", ArticleLikeCount.class);
    }

}
