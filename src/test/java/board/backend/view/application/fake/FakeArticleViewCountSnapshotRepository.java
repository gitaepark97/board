package board.backend.view.application.fake;

import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.view.domain.ArticleViewCountSnapshot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeArticleViewCountSnapshotRepository implements ArticleCountSnapshotRepository<ArticleViewCountSnapshot> {

    private final Map<LocalDate, Map<Long, ArticleViewCountSnapshot>> store = new HashMap<>();

    @Override
    public Optional<ArticleViewCountSnapshot> findByDateAndArticleId(LocalDate date, Long articleId) {
        return Optional.ofNullable(store.getOrDefault(date, Map.of()).get(articleId));
    }

    @Override
    public void saveAll(List<ArticleViewCountSnapshot> snapshots) {
        for (ArticleViewCountSnapshot snapshot : snapshots) {
            store
                .computeIfAbsent(snapshot.getDate(), k -> new HashMap<>())
                .put(snapshot.getArticleId(), snapshot);
        }
    }

}
