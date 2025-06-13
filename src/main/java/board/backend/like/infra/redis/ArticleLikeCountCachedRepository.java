package board.backend.like.infra.redis;

import board.backend.common.infra.AbstractRedisCachedRepository;
import board.backend.like.domain.ArticleLikeCount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleLikeCountCachedRepository extends AbstractRedisCachedRepository<ArticleLikeCount, Long> {

    protected ArticleLikeCountCachedRepository(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "like::article::%s", ArticleLikeCount.class);
    }

}
