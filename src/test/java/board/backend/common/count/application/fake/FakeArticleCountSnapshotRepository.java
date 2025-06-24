package board.backend.common.count.application.fake;

import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.count.domain.ArticleCountSnapshot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeArticleCountSnapshotRepository<T extends ArticleCountSnapshot> implements ArticleCountSnapshotRepository<T> {

    private final Map<String, T> store = new HashMap<>();

    @Override
    public Optional<T> findByDateAndArticleId(LocalDate date, Long articleId) {
        return Optional.ofNullable(store.get(key(date, articleId)));
    }

    @Override
    public void saveAll(List<T> snapshots) {
        snapshots.forEach(this::save);
    }

    public void save(T snapshot) {
        store.put(key(snapshot.getDate(), snapshot.getArticleId()), snapshot);
    }

    private String key(LocalDate date, Long articleId) {
        return date + "::" + articleId;
    }

}
