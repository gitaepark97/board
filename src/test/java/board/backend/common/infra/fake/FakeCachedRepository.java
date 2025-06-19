package board.backend.common.infra.fake;

import board.backend.common.infra.CachedRepository;

import java.time.Duration;
import java.util.*;

public class FakeCachedRepository<T, K> implements CachedRepository<T, K> {

    private final Map<K, T> store = new HashMap<>();

    @Override
    public boolean existsByKey(K key) {
        return store.containsKey(key);
    }

    @Override
    public Optional<T> findByKey(K key) {
        return Optional.ofNullable(store.get(key));
    }

    @Override
    public List<T> findAllByKey(List<K> keys) {
        List<T> result = new ArrayList<>();
        for (K key : keys) {
            T value = store.get(key);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    @Override
    public void save(K key, T value, Duration ttl) {
        store.put(key, value);
    }

    @Override
    public void delete(K key) {
        store.remove(key);
    }

}
