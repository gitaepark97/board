package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleCommentCountChangedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleCommentCountChangedEventHandler implements EventHandler<ArticleCommentCountChangedEventPayload> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<ArticleCommentCountChangedEventPayload> event) {
        ArticleCommentCountChangedEventPayload payload = event.getPayload();
        hotArticleScoreScoreCalculator.saveArticleCommentCount(payload.articleId(), payload.count(), payload.changedAt());
    }

    @Override
    public boolean supports(Event<ArticleCommentCountChangedEventPayload> event) {
        return EventType.ARTICLE_COMMENT_COUNT_CHANGED == event.getType();
    }

}
