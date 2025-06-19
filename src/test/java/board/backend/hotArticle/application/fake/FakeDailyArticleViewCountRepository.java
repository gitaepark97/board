package board.backend.hotArticle.application.fake;

import board.backend.hotArticle.application.port.DailyArticleViewCountRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FakeDailyArticleViewCountRepository implements DailyArticleViewCountRepository {

    private final Map<Key, Long> store = new HashMap<>();

    @Override
    public Long read(Long articleId, LocalDateTime time) {
        return store.getOrDefault(new Key(articleId, time), 0L);
    }

    @Override
    public void save(Long articleId, Long count, LocalDateTime time, Duration ttl) {
        store.put(new Key(articleId, time), count);
    }

    @Override
    public void deleteById(Long articleId) {
        store.keySet().removeIf(key -> key.articleId.equals(articleId));
    }

    private record Key(Long articleId, LocalDateTime time) {

    }

}
