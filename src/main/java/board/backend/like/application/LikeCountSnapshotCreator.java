package board.backend.like.application;

import board.backend.common.count.application.AbstractArticleCountSnapshotCreator;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class LikeCountSnapshotCreator extends AbstractArticleCountSnapshotCreator<ArticleLikeCount, ArticleLikeCountSnapshot> {

    LikeCountSnapshotCreator(
        TimeProvider timeProvider,
        ArticleLikeCountRepository articleCountRepository,
        ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountRepository, articleCountSnapshotRepository);
    }

    @Override
    protected ArticleLikeCountSnapshot toSnapshot(ArticleLikeCount articleCount, LocalDate snapshotDate) {
        return articleCount.toSnapshot(snapshotDate);
    }

}
