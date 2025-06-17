package board.backend.hotArticle.infra.redis;

import board.backend.hotArticle.application.port.DailyArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
class DailyArticleViewCountRepositoryImpl implements DailyArticleViewCountRepository {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String KEY_FORMAT = "view::article::%s::date::%s";

    private final StringRedisTemplate redisTemplate;

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

    private String generateKey(Long articleId, LocalDateTime time) {
        return String.format(KEY_FORMAT, articleId, TIME_FORMATTER.format(time));
    }

}
