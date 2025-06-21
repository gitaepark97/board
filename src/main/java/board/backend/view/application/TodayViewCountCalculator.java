package board.backend.view.application;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.view.domain.ArticleViewCountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class TodayViewCountCalculator {

    private final TimeProvider timeProvider;
    private final ArticleCountSnapshotRepository<ArticleViewCountSnapshot> articleCountSnapshotRepository;

    long calculate(Long articleId, Long currentCount) {
        long yesterdayCount = articleCountSnapshotRepository
            .findByDateAndArticleId(timeProvider.yesterday(), articleId)
            .map(ArticleViewCountSnapshot::viewCount)
            .orElse(0L);
        return currentCount - yesterdayCount;
    }

}
