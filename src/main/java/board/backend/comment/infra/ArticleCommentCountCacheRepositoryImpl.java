package board.backend.comment.infra;

import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.infra.AbstractRedisCacheRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class ArticleCommentCountCacheRepositoryImpl extends AbstractRedisCacheRepository<ArticleCommentCount, Long> {

    protected ArticleCommentCountCacheRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "comment::article::%s", ArticleCommentCount.class);
    }

}
