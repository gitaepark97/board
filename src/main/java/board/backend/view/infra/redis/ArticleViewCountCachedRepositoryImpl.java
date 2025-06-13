package board.backend.view.infra.redis;

import board.backend.common.infra.AbstractRedisCachedRepository;
import board.backend.view.domain.ArticleViewCount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleViewCountCachedRepositoryImpl extends AbstractRedisCachedRepository<ArticleViewCount, Long> {

    protected ArticleViewCountCachedRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "view::article::%s", ArticleViewCount.class);
    }

}
