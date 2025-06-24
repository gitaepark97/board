package board.backend.common.cache;

import board.backend.common.cache.infra.AbstractRedisCachedRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.redis.core.RedisTemplate;

@TestComponent
class DummyRedisCachedRepository extends AbstractRedisCachedRepository<Dummy, Long> {

    DummyRedisCachedRepository(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "dummy:%d", Dummy.class, new ObjectMapper());
    }

}

record Dummy(Long id, String name) {

}
