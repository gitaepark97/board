package board.backend.view.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
class ArticleViewDistributedLockRepositoryImpl implements ArticleViewDistributedLockRepository {

    private static final String KEY_FORMAT = "view::article::%s::ip::%s::lock";

    private final StringRedisTemplate redisTemplate;

    @Override
    public Boolean lock(Long articleId, String ip, Duration ttl) {
        String key = generateKey(articleId, ip);
        return redisTemplate.opsForValue().setIfAbsent(key, "", ttl);
    }

    private String generateKey(Long articleId, String ip) {
        return String.format(KEY_FORMAT, articleId, ip);
    }

}
