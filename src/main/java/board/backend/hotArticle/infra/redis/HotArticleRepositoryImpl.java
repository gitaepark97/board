package board.backend.hotArticle.infra.redis;

import board.backend.hotArticle.application.port.HotArticleRepository;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
class HotArticleRepositoryImpl implements HotArticleRepository {

    private static final String KEY_FORMAT = "hot-article::list::%s";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final StringRedisTemplate redisTemplate;

    HotArticleRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Long> readAll(String dateStr) {
        return Objects.requireNonNull(redisTemplate.opsForZSet()
                .reverseRangeWithScores(generateKey(dateStr), 0, -1))
            .stream()
            .map(ZSetOperations.TypedTuple::getValue)
            .filter(Objects::nonNull)
            .map(Long::parseLong)
            .toList();
    }

    @Override
    public void save(Long articleId, LocalDateTime createdAt, Long score, Long limit, Duration ttl) {
        redisTemplate.executePipelined((RedisCallback<?>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;
            String key = generateKey(createdAt);
            conn.zAdd(key, score, String.valueOf(articleId));
            conn.zRemRange(key, 0, -limit - 1);
            conn.expire(key, ttl.toSeconds());
            return null;
        });
    }

    @Override
    public void delete(Long articleId) {
        Set<String> keys = redisTemplate.keys(KEY_FORMAT.formatted("*"));
        for (String key : keys) {
            redisTemplate.opsForZSet().remove(key, String.valueOf(articleId));
        }
    }

    private String generateKey(String dateStr) {
        return KEY_FORMAT.formatted(dateStr);
    }

    private String generateKey(LocalDateTime time) {
        return generateKey(TIME_FORMATTER.format(time));
    }

}
