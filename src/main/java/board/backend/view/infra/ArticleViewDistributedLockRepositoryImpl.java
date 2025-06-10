package board.backend.view.infra;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
class ArticleViewDistributedLockRepositoryImpl implements ArticleViewDistributedLockRepository {

    private static final String KEY_FORMAT = "view::article::%s::ip::%s::lock";

    private final StringRedisTemplate locks;

    ArticleViewDistributedLockRepositoryImpl(@Qualifier("lockRedisTemplate") StringRedisTemplate locks) {
        this.locks = locks;
    }

    @Override
    public Boolean lock(Long articleId, String ip, Duration ttl) {
        String key = generateKey(articleId, ip);
        return locks.opsForValue().setIfAbsent(key, "", ttl);
    }

    private String generateKey(Long articleId, String ip) {
        return String.format(KEY_FORMAT, articleId, ip);
    }

}
