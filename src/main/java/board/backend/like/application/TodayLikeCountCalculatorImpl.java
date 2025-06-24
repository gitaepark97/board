package board.backend.like.application;

import board.backend.common.count.application.AbstractTodayCountCalculator;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import org.springframework.stereotype.Component;

@Component
class TodayLikeCountCalculatorImpl extends AbstractTodayCountCalculator<ArticleLikeCountSnapshot> {

    TodayLikeCountCalculatorImpl(
        TimeProvider timeProvider,
        ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountSnapshotRepository);
    }

}
