package board.backend.comment.application;

import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.count.application.AbstractTodayCountCalculator;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import org.springframework.stereotype.Component;

@Component
class TodayCommentCountCalculator extends AbstractTodayCountCalculator<ArticleCommentCountSnapshot> {

    TodayCommentCountCalculator(
        TimeProvider timeProvider,
        ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountSnapshotRepository);
    }

}
