package board.backend.common.count.application;

import board.backend.common.count.application.port.ArticleCountRepository;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;

import java.time.LocalDate;

class DummyArticleCountSnapshotCreator extends AbstractArticleCountSnapshotCreator<DummyArticleCount, DummyArticleCountSnapshot> {

    DummyArticleCountSnapshotCreator(
        TimeProvider timeProvider,
        ArticleCountRepository<DummyArticleCount> articleCountRepository,
        ArticleCountSnapshotRepository<DummyArticleCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountRepository, articleCountSnapshotRepository);
    }

    @Override
    protected DummyArticleCountSnapshot toSnapshot(DummyArticleCount articleCount, LocalDate snapshotDate) {
        return articleCount.toSnapshot(snapshotDate);
    }

}
