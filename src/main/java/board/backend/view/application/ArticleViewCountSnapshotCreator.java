package board.backend.view.application;

import board.backend.common.count.application.AbstractArticleCountSnapshotCreator;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import board.backend.view.domain.ArticleViewCountSnapshot;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class ArticleViewCountSnapshotCreator extends AbstractArticleCountSnapshotCreator<ArticleViewCount, ArticleViewCountSnapshot> {

    ArticleViewCountSnapshotCreator(
        TimeProvider timeProvider,
        ArticleViewCountRepository articleCountRepository,
        ArticleCountSnapshotRepository<ArticleViewCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountRepository, articleCountSnapshotRepository);
    }

    @Override
    protected ArticleViewCountSnapshot toSnapshot(ArticleViewCount articleCount, LocalDate snapshotDate) {
        return articleCount.toSnapshot(snapshotDate);
    }

}
