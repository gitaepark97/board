package board.backend.view.infra.redis;

import board.backend.view.application.port.ArticleViewBackupTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
class ArticleViewCountBackupTimeRepositoryImpl implements ArticleViewBackupTimeRepository {

    private static final String KEY_FORMAT = "view::article::%s::last-backup-time";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final StringRedisTemplate redisTemplate;

    @Override
    public void update(Long articleId, LocalDateTime backupTime) {
        String key = String.format(KEY_FORMAT, articleId);
        redisTemplate.opsForValue().set(key, backupTime.format(FORMATTER));
    }

    @Override
    public LocalDateTime findById(Long articleId) {
        String key = generateKey(articleId);
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? null : LocalDateTime.parse(value, FORMATTER);
    }

    private String generateKey(Long articleId) {
        return String.format(KEY_FORMAT, articleId);
    }

}
