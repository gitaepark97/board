package board.backend.user.infra.redis;

import board.backend.common.infra.AbstractRedisCachedRepository;
import board.backend.user.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class CachedUserRepositoryImpl extends AbstractRedisCachedRepository<User, Long> {

    CachedUserRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "user::%s", User.class);
    }

}
