package board.backend.hotArticle.infra.redis;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
    public void decrease(Long articleId, LocalDateTime time) {
        String key = generateKey(articleId, time);

        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().decrement(key);
        }
    }

    private String generateKey(Long articleId, LocalDateTime time) {
        return String.format(keyFormat, articleId, TIME_FORMATTER.format(time));
    }

}
