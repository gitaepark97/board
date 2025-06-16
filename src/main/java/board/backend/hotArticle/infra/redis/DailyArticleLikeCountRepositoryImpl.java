package board.backend.hotArticle.infra.redis;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Primary
@Component
class DailyArticleLikeCountRepositoryImpl extends DailyArticleCountRepositoryImpl {

    DailyArticleLikeCountRepositoryImpl(StringRedisTemplate redisTemplate) {
        super("like::article::%s::date::%s", redisTemplate);
    }

}
