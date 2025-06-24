package board.backend.comment.application;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.count.application.AbstractArticleCountSnapshotCreator;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.common.support.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class CommentCountSnapshotCreator extends AbstractArticleCountSnapshotCreator<ArticleCommentCount, ArticleCommentCountSnapshot> {

    CommentCountSnapshotCreator(
        TimeProvider timeProvider,
        ArticleCommentCountRepository articleCountRepository,
        ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> articleCountSnapshotRepository
    ) {
        super(timeProvider, articleCountRepository, articleCountSnapshotRepository);
    }

    @Override
    protected ArticleCommentCountSnapshot toSnapshot(ArticleCommentCount articleCount, LocalDate snapshotDate) {
        return articleCount.toSnapshot(snapshotDate);
    }

}
