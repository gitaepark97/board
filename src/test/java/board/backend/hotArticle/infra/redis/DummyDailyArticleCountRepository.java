package board.backend.hotArticle.infra.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

class DummyDailyArticleCountRepository extends DailyArticleCountRepositoryImpl {

    DummyDailyArticleCountRepository(StringRedisTemplate redisTemplate) {
        super("dummy:article:daily:%d:%s", redisTemplate);
    }

}
