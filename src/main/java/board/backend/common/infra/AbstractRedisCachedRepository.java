package board.backend.common.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRedisCachedRepository<V, K> implements CachedRepository<V, K> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String keyPrefix;
    private final Class<V> valueType;

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected AbstractRedisCachedRepository(
        RedisTemplate<String, Object> redisTemplate,
        String keyPrefix,
        Class<V> valueType
    ) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.valueType = valueType;
    }

    @Override
    public boolean existsByKey(K key) {
        return redisTemplate.hasKey(generateKey(key));
    }

    @Override
    public Optional<V> findByKey(K key) {
        Object value = redisTemplate.opsForValue().get(generateKey(key));
        if (valueType.isInstance(value)) {
            return Optional.of(valueType.cast(value));
        }
        return Optional.empty();
    }

    @Override
    public List<V> finalAllByKey(List<K> keys) {
        List<String> redisKeys = keys.stream()
            .map(this::generateKey)
            .toList();

        List<Object> values = redisTemplate.opsForValue().multiGet(redisKeys);
        if (values == null) return List.of();

        return values.stream()
            .filter(valueType::isInstance)
            .map(valueType::cast)
            .toList();
    }

    @Override
    public void save(K key, V value, Duration ttl) {
        redisTemplate.opsForValue().set(generateKey(key), value, ttl);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(generateKey(key));
    }

    protected String generateKey(K key) {
        return String.format(keyPrefix, key);
    }

}
