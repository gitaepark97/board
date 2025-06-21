package board.backend.comment.application;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class CommentCountSnapshotCreator {

    private final TimeProvider timeProvider;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> articleCommentCountSnapshotRepository;

    void createCountSnapshot() {
        List<ArticleCommentCountSnapshot> snapshots = articleCommentCountRepository.findAll().stream()
            .map(commentCount -> toSnapshot(commentCount, timeProvider.yesterday()))
            .flatMap(Optional::stream)
            .toList();

        articleCommentCountSnapshotRepository.saveAll(snapshots);
    }

    private Optional<ArticleCommentCountSnapshot> toSnapshot(
        ArticleCommentCount commentCount,
        LocalDate snapshotDate
    ) {
        if (commentCount.commentCount() == 0L) {
            return Optional.empty();
        }

        return Optional.of(new ArticleCommentCountSnapshot(snapshotDate, commentCount.articleId(), commentCount.commentCount()));
    }

}
