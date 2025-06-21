package board.backend.comment.application.fake;

import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.application.port.ArticleCountSnapshotRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeArticleCommentCountSnapshotRepository implements ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> {

    private final Map<LocalDate, Map<Long, ArticleCommentCountSnapshot>> store = new HashMap<>();

    @Override
    public Optional<ArticleCommentCountSnapshot> findByDateAndArticleId(LocalDate date, Long articleId) {
        return Optional.ofNullable(store.getOrDefault(date, Map.of()).get(articleId));
    }

    @Override
    public void saveAll(List<ArticleCommentCountSnapshot> snapshots) {
        for (ArticleCommentCountSnapshot snapshot : snapshots) {
            store
                .computeIfAbsent(snapshot.date(), k -> new HashMap<>())
                .put(snapshot.articleId(), snapshot);
        }
    }

}
