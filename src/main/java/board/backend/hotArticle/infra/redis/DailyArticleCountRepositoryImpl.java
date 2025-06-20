package board.backend.hotArticle.infra.redis;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


abstract class DailyArticleCountRepositoryImpl implements DailyArticleCountRepository {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final String keyFormat;
    private final StringRedisTemplate redisTemplate;

    DailyArticleCountRepositoryImpl(String keyFormat, StringRedisTemplate redisTemplate) {
        this.keyFormat = keyFormat;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long read(Long articleId, LocalDateTime time) {
        String key = generateKey(articleId, time);
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0L;
    }

    @Override
    public void increaseOrSave(Long articleId, LocalDateTime time, Duration ttl) {
        String key = generateKey(articleId, time);
        redisTemplate.opsForValue().increment(key);

        boolean hasTtl = redisTemplate.getExpire(key) > 0;
        if (!hasTtl) {
            redisTemplate.expire(key, ttl);
        }
    }

    @Override
    public void increaseOrSave(Long articleId, Long increasement, LocalDateTime time, Duration ttl) {
        String key = generateKey(articleId, time);
        redisTemplate.opsForValue().increment(key, increasement);

        boolean hasTtl = redisTemplate.getExpire(key) > 0;
        if (!hasTtl) {
            redisTemplate.expire(key, ttl);
        }
    }

    @Override
    public void decrease(Long articleId, LocalDateTime now) {
        String key = generateKey(articleId, now);

        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().decrement(key);
        }
    }

    @Override
    public void deleteById(Long articleId) {
        Set<String> keys = redisTemplate.keys(keyFormat.formatted(articleId, "*"));
        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }

    private String generateKey(Long articleId, LocalDateTime time) {
        return keyFormat.formatted(articleId, TIME_FORMATTER.format(time));
    }

}
