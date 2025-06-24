package board.backend.view.infra.redis;

import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
class ArticleViewCountRepositoryImpl implements ArticleViewCountRepository {

    private static final String KEY_FORMAT = "view::article::%s";
    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<ArticleViewCount> findById(Long articleId) {
        String key = generateKey(articleId);
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(ArticleViewCount.create(articleId, Long.parseLong(value)));
    }

    @Override
    public List<ArticleViewCount> findAll() {
        List<String> keys = new ArrayList<>();

        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
            .getConnection()
            .scan(ScanOptions.scanOptions().match(KEY_FORMAT.formatted("*")).build());

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        if (keys.isEmpty()) return List.of();

        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) return List.of();

        return IntStream.range(0, keys.size())
            .filter(i -> values.get(i) != null)
            .mapToObj(i -> {
                String key = keys.get(i);
                Long articleId = extractArticleIdFromKey(key);
                Long count = Long.parseLong(values.get(i));
                return ArticleViewCount.create(articleId, count);
            })
            .toList();
    }

    @Override
    public List<ArticleViewCount> findAllById(List<Long> articleIds) {
        List<String> keys = articleIds.stream()
            .map(this::generateKey)
            .toList();

        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return List.of();
        }

        return IntStream.range(0, articleIds.size())
            .mapToObj(i -> {
                String value = values.get(i);
                if (value == null) return null;
                Long articleId = articleIds.get(i);
                long viewCount = Long.parseLong(value);
                return ArticleViewCount.create(articleId, viewCount);
            })
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public void deleteById(Long articleId) {
        String key = generateKey(articleId);
        redisTemplate.delete(key);
    }

    @Override
    public Long increase(Long articleId) {
        String key = generateKey(articleId);
        return redisTemplate.opsForValue().increment(key);
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }

    private Long extractArticleIdFromKey(String key) {
        String[] parts = key.split("::");
        return Long.parseLong(parts[2]);
    }

}
