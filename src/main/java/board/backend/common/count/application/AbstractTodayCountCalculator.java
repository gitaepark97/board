package board.backend.common.count.application;

import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.count.domain.ArticleCount;
import board.backend.common.count.domain.ArticleCountSnapshot;
import board.backend.common.support.TimeProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractTodayCountCalculator<T extends ArticleCountSnapshot> implements TodayCountCalculator {

    private final TimeProvider timeProvider;
    private final ArticleCountSnapshotRepository<T> articleCountSnapshotRepository;

    @Override
    public long calculate(ArticleCount articleCount) {
        long yesterdayCount = articleCountSnapshotRepository
            .findByDateAndArticleId(timeProvider.yesterday(), articleCount.getArticleId())
            .map(T::getCount)
            .orElse(0L);
        return articleCount.getCount() - yesterdayCount;
    }

}
