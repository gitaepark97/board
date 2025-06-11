package board.backend.common.infra;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface CacheRepository<V, K> {

    Optional<V> get(K key);

    List<V> getAll(List<K> keys);

    void set(K key, V value, Duration ttl);

    void delete(K key);

}
