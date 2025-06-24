package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleLikeCountChangedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeCountChangedEventHandler implements EventHandler<ArticleLikeCountChangedEventPayload> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<ArticleLikeCountChangedEventPayload> event) {
        ArticleLikeCountChangedEventPayload payload = event.getPayload();
        hotArticleScoreScoreCalculator.saveArticleLikeCount(payload.articleId(), payload.count(), payload.changedAt());
    }

    @Override
    public boolean supports(Event<ArticleLikeCountChangedEventPayload> event) {
        return EventType.ARTICLE_LIKE_COUNT_CHANGED == event.getType();
    }

}
