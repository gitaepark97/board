package board.backend.view.application;

import board.backend.common.count.application.AbstractTodayCountCalculator;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.view.domain.ArticleViewCountSnapshot;
import org.springframework.stereotype.Component;

@Component
class TodayViewCountCalculatorImpl extends AbstractTodayCountCalculator<ArticleViewCountSnapshot> {

    TodayViewCountCalculatorImpl(
        TimeProvider timeProvider,
        ArticleCountSnapshotRepository<ArticleViewCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountSnapshotRepository);
    }

}
