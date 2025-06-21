package board.backend.comment.application;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class TodayCommentCountCalculator {

    private final TimeProvider timeProvider;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> articleCommentCountSnapshotRepository;

    public long calculate(Long articleId) {
        long currentCount = articleCommentCountRepository.findById(articleId)
            .map(ArticleCommentCount::commentCount)
            .orElse(0L);

        long yesterdayCount = articleCommentCountSnapshotRepository
            .findByDateAndArticleId(timeProvider.yesterday(), articleId)
            .map(ArticleCommentCountSnapshot::commentCount)
            .orElse(0L);

        return Math.max(currentCount - yesterdayCount, 0L);
    }

}
