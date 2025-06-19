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
    public void increaseOrSave(Long articleId, LocalDateTime now, Duration ttl) {
        Key key = new Key(articleId, toDate(now));
        store.put(key, store.getOrDefault(key, 0L) + 1);
    }

    @Override
    public void decrease(Long articleId, LocalDateTime now) {
        Key key = new Key(articleId, toDate(now));
        store.computeIfPresent(key, (k, v) -> Math.max(0L, v - 1));
    }

    @Override
    public void deleteById(Long articleId) {
        store.keySet().removeIf(key -> key.articleId.equals(articleId));
    }

    private LocalDate toDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    private record Key(Long articleId, LocalDate date) {

    }

}