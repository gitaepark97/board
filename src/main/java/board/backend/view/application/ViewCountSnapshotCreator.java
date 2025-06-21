package board.backend.view.application;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import board.backend.view.domain.ArticleViewCountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class ViewCountSnapshotCreator {

    private final TimeProvider timeProvider;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleCountSnapshotRepository<ArticleViewCountSnapshot> articleCountSnapshotRepository;

    void createCountSnapshot() {
        List<ArticleViewCountSnapshot> snapshots = articleViewCountRepository.findAll().stream()
            .map(viewCount -> toSnapshot(viewCount, timeProvider.yesterday()))
            .flatMap(Optional::stream)
            .toList();

        articleCountSnapshotRepository.saveAll(snapshots);
    }

    private Optional<ArticleViewCountSnapshot> toSnapshot(
        ArticleViewCount viewCount,
        LocalDate snapshotDate
    ) {
        if (viewCount.viewCount() == 0L) {
            return Optional.empty();
        }

        return Optional.of(new ArticleViewCountSnapshot(snapshotDate, viewCount.articleId(), viewCount.viewCount()));
    }

}
