package board.backend.hotArticle.application.fake;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FakeDailyArticleCountRepository implements DailyArticleCountRepository {

    private final Map<Key, Long> store = new HashMap<>();

    @Override
    public Long read(Long articleId, LocalDateTime time) {
        return store.getOrDefault(new Key(articleId, toDate(time)), 0L);
    }

    @Override
    public void save(Long articleId, Long count, LocalDateTime time, Duration ttl) {
        store.put(new Key(articleId, toDate(time)), count);
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        store.keySet().removeIf(key -> key.articleId.equals(articleId));
    }

    private LocalDate toDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    private record Key(Long articleId, LocalDate date) {

    }

}