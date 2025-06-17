package board.backend.view.infra.redis;

import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
class ArticleViewCountRepositoryImpl implements ArticleViewCountRepository {

    private static final String KEY_FORMAT = "view::article::%s";
    private final StringRedisTemplate redisTemplate;

    @Override
    public Long findById(Long articleId) {
        String key = generateKey(articleId);
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            // 이미 생성된 게시물 존재
            redisTemplate.opsForValue().set(key, "0");
            return 0L;
        }

        return Long.parseLong(value);
    }

    @Override
    public Map<Long, Long> findAllById(List<Long> articleIds) {
        List<String> keys = articleIds.stream()
            .map(this::generateKey)
            .toList();

        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        assert values != null;

        return IntStream.range(0, articleIds.size())
            .boxed()
            .collect(Collectors.toMap(
                articleIds::get,
                i -> {
                    String value = values.get(i);
                    // 이미 생성된 게시물 존재
                    return value == null ? 0L : Long.parseLong(value);
                }
            ));
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

    @Override
    public void save(ArticleViewCount articleViewCount) {
        String key = generateKey(articleViewCount.articleId());
        redisTemplate.opsForValue().set(key, String.valueOf(articleViewCount.viewCount()));
    }

    private String generateKey(Long articleId) {
        return String.format(KEY_FORMAT, articleId);
    }

}
