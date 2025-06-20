package board.backend.view.infra.redis;

import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
class ArticleViewDistributedLockRepositoryImpl implements ArticleViewDistributedLockRepository {

    private static final String KEY_FORMAT = "view::article::%s::ip::%s::lock";

    private final StringRedisTemplate distributedLocks;

    ArticleViewDistributedLockRepositoryImpl(@Qualifier("lockStringRedisTemplate") StringRedisTemplate redisTemplate) {
        this.distributedLocks = redisTemplate;
    }

    @Override
    public Boolean lock(Long articleId, String ip, Duration ttl) {
        String key = generateKey(articleId, ip);
        return distributedLocks.opsForValue().setIfAbsent(key, "", ttl);
    }

    private String generateKey(Long articleId, String ip) {
        return KEY_FORMAT.formatted(articleId, ip);
    }

}
