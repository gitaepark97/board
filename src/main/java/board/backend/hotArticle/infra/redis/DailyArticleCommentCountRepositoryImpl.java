package board.backend.hotArticle.infra.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Qualifier("dailyArticleCommentCountRepository")
@Component
class DailyArticleCommentCountRepositoryImpl extends DailyArticleCountRepositoryImpl {

    DailyArticleCommentCountRepositoryImpl(StringRedisTemplate redisTemplate) {
        super("comment::article::%s::date::%s", redisTemplate);
    }

}
