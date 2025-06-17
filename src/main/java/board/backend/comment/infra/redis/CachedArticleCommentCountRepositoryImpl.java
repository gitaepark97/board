package board.backend.comment.infra.redis;

import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.infra.AbstractRedisCachedRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class CachedArticleCommentCountRepositoryImpl extends AbstractRedisCachedRepository<ArticleCommentCount, Long> {

    protected CachedArticleCommentCountRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "comment::article::%s", ArticleCommentCount.class);
    }

}
