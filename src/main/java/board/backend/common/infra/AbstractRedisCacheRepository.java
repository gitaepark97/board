package board.backend.common.infra;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRedisCacheRepository<V, K> implements CacheRepository<V, K> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String keyPrefix;
    private final Class<V> valueType;

    protected AbstractRedisCacheRepository(
        RedisTemplate<String, Object> redisTemplate,
        String keyPrefix,
        Class<V> valueType
    ) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.valueType = valueType;
    }

    @Override
    public Optional<V> get(K key) {
        Object value = redisTemplate.opsForValue().get(generateKey(key));
        if (valueType.isInstance(value)) {
            return Optional.of(valueType.cast(value));
        }
        return Optional.empty();
    }

    @Override
    public List<V> getAll(List<K> keys) {
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
    public void set(K key, V value, Duration ttl) {
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
