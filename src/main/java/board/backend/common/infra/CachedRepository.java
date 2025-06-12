package board.backend.common.infra;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface CachedRepository<V, K> {

    boolean existsByKey(K key);

    Optional<V> findByKey(K key);

    List<V> finalAllByKey(List<K> keys);

    void save(K key, V value, Duration ttl);

    void delete(K key);

}
