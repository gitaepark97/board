package board.backend.common.count.application;

import board.backend.common.count.application.port.ArticleCountRepository;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.count.domain.ArticleCount;
import board.backend.common.count.domain.ArticleCountSnapshot;
import board.backend.common.support.TimeProvider;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractArticleCountSnapshotCreator<T extends ArticleCount, K extends ArticleCountSnapshot> {

    private final TimeProvider timeProvider;
    private final ArticleCountRepository<T> articleCountRepository;
    private final ArticleCountSnapshotRepository<K> articleCountSnapshotRepository;

    public void createCountSnapshot() {
        List<K> snapshots = articleCountRepository.findAll().stream()
            .map(articleCount -> toSnapshot(articleCount, timeProvider.yesterday()))
            .filter(Objects::nonNull)
            .toList();

        articleCountSnapshotRepository.saveAll(snapshots);
    }

    protected abstract K toSnapshot(T articleCount, LocalDate snapshotDate);

}
