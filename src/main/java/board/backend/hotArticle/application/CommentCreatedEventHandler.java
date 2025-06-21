package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.CommentCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("hotArticleCommentCreatedEventHandler")
class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        CommentCreatedEventPayload payload = event.getPayload();
        hotArticleScoreScoreCalculator.saveArticleCommentCount(payload.articleId(), payload.commentCount(), payload.createdAt());
    }

    @Override
    public boolean supports(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED == event.getType();
    }

}
