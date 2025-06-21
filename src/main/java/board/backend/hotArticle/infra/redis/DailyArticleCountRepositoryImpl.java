package board.backend.hotArticle.infra.redis;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
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
    public void save(Long articleId, Long count, LocalDateTime time, Duration ttl) {
        String key = generateKey(articleId, time);
        redisTemplate.opsForValue().set(key, String.valueOf(count), ttl);
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        String pattern = keyFormat.formatted(articleId, "*");

        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
            .getConnection()
            .scan(ScanOptions.scanOptions().match(pattern).build());

        while (cursor.hasNext()) {
            byte[] keyBytes = cursor.next();
            String key = new String(keyBytes);
            redisTemplate.delete(key);
        }
    }

    private String generateKey(Long articleId, LocalDateTime time) {
        return keyFormat.formatted(articleId, TIME_FORMATTER.format(time));
    }

}
