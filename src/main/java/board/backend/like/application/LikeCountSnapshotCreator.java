package board.backend.like.application;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class LikeCountSnapshotCreator {

    private final TimeProvider timeProvider;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> articleLikeCountSnapshotRepository;

    void createCountSnapshot() {
        List<ArticleLikeCountSnapshot> snapshots = articleLikeCountRepository.findAll().stream()
            .map(likeCount -> toSnapshot(likeCount, timeProvider.yesterday()))
            .flatMap(Optional::stream)
            .toList();

        articleLikeCountSnapshotRepository.saveAll(snapshots);
    }

    private Optional<ArticleLikeCountSnapshot> toSnapshot(
        ArticleLikeCount likeCount,
        LocalDate snapshotDate
    ) {
        if (likeCount.likeCount() == 0L) {
            return Optional.empty();
        }

        return Optional.of(new ArticleLikeCountSnapshot(snapshotDate, likeCount.articleId(), likeCount.likeCount()));
    }

}
