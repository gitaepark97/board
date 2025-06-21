package board.backend.like.application.fake;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.like.domain.ArticleLikeCountSnapshot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeArticleLikeCountSnapshotRepository implements ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> {

    private final Map<LocalDate, Map<Long, ArticleLikeCountSnapshot>> store = new HashMap<>();

    @Override
    public Optional<ArticleLikeCountSnapshot> findByDateAndArticleId(LocalDate date, Long articleId) {
        return Optional.ofNullable(store.getOrDefault(date, Map.of()).get(articleId));
    }

    @Override
    public void saveAll(List<ArticleLikeCountSnapshot> snapshots) {
        for (ArticleLikeCountSnapshot snapshot : snapshots) {
            store
                .computeIfAbsent(snapshot.date(), k -> new HashMap<>())
                .put(snapshot.articleId(), snapshot);
        }
    }

}
