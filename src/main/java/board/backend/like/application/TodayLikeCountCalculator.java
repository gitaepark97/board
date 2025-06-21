package board.backend.like.application;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class TodayLikeCountCalculator {

    private final TimeProvider timeProvider;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> articleLikeCountSnapshotRepository;

    long calculate(Long articleId) {
        long currentCount = articleLikeCountRepository.findById(articleId)
            .map(ArticleLikeCount::likeCount)
            .orElse(0L);

        long yesterdayCount = articleLikeCountSnapshotRepository
            .findByDateAndArticleId(timeProvider.yesterday(), articleId)
            .map(ArticleLikeCountSnapshot::likeCount)
            .orElse(0L);

        return Math.max(currentCount - yesterdayCount, 0L);
    }

}
