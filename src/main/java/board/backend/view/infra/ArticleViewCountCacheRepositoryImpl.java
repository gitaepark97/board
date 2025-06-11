package board.backend.view.infra;

import board.backend.common.infra.AbstractRedisCacheRepository;
import board.backend.view.domain.ArticleViewCount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleViewCountCacheRepositoryImpl extends AbstractRedisCacheRepository<ArticleViewCount, Long> {

    protected ArticleViewCountCacheRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "view::article::%s", ArticleViewCount.class);
    }

}
