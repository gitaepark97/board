package board.backend.common.count.application;

import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.count.domain.ArticleCount;
import board.backend.common.count.domain.ArticleCountSnapshot;
import board.backend.common.support.TimeProvider;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

class DummyTodayCountCalculator extends AbstractTodayCountCalculator<DummyArticleCountSnapshot> {

    DummyTodayCountCalculator(
        TimeProvider timeProvider,
        ArticleCountSnapshotRepository<DummyArticleCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountSnapshotRepository);
    }

}

@SuperBuilder
class DummyArticleCount extends ArticleCount {

    static DummyArticleCount create(Long articleId, Long count) {
        return DummyArticleCount.builder().articleId(articleId).count(count).build();
    }

    DummyArticleCountSnapshot toSnapshot(LocalDate date) {
        return count != 0L ? DummyArticleCountSnapshot.create(date, articleId, count) : null;
    }

}

@SuperBuilder
class DummyArticleCountSnapshot extends ArticleCountSnapshot {

    static DummyArticleCountSnapshot create(LocalDate date, Long articleId, Long count) {
        return DummyArticleCountSnapshot.builder().date(date).articleId(articleId).count(count).build();
    }

}