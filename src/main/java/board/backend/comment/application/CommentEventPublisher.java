package board.backend.comment.application;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleCommentCountChangedEventPayload;
import board.backend.common.exception.InternalServerError;
import board.backend.common.support.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CommentEventPublisher {

    private final TimeProvider timeProvider;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final EventPublisher eventPublisher;
    private final TodayCommentCountCalculator todayCommentCountCalculator;

    void publishEvent(Comment comment) {
        ArticleCommentCount articleCommentCount = articleCommentCountRepository.findById(comment.articleId())
            .orElseThrow(InternalServerError::new);
        long todayCount = todayCommentCountCalculator.calculate(articleCommentCount);
        eventPublisher.publishEvent(EventType.ARTICLE_COMMENT_COUNT_CHANGED, new ArticleCommentCountChangedEventPayload(comment.articleId(), todayCount, timeProvider.now()));
    }

}
