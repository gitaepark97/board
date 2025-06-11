package board.backend.user.infra;

import board.backend.common.infra.AbstractRedisCacheRepository;
import board.backend.user.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class UserCacheRepositoryImpl extends AbstractRedisCacheRepository<User, Long> {

    UserCacheRepositoryImpl(@Qualifier("cacheRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, "user::%s", User.class);
    }

}
