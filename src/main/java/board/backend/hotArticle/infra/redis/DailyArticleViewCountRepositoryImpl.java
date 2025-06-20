package board.backend.hotArticle.infra.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Qualifier("dailyArticleViewCountRepository")
class DailyArticleViewCountRepositoryImpl extends DailyArticleCountRepositoryImpl {

    DailyArticleViewCountRepositoryImpl(StringRedisTemplate redisTemplate) {
        super("view::article::%s::date::%s", redisTemplate);
    }

}
