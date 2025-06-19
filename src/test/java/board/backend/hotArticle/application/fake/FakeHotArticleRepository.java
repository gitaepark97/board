package board.backend.hotArticle.application.fake;

import board.backend.hotArticle.application.port.HotArticleRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FakeHotArticleRepository implements HotArticleRepository {

    private final Map<Long, Entry> store = new HashMap<>();

    @Override
    public List<Long> readAll(String dateStr) {
        return store.values().stream()
            .sorted(Comparator.comparingLong(Entry::score).reversed())
            .map(Entry::articleId)
            .toList();
    }

    @Override
    public void save(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl) {
        store.put(articleId, new Entry(articleId, time, score));
    }

    @Override
    public void delete(Long articleId) {
        store.remove(articleId);
    }

    public Optional<Entry> findById(Long articleId) {
        return Optional.ofNullable(store.get(articleId));
    }

    public record Entry(Long articleId, LocalDateTime time, Long score) {

    }

}
