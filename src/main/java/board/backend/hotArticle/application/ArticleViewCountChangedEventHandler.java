package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleViewCountChangedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleViewCountChangedEventHandler implements EventHandler<ArticleViewCountChangedEventPayload> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<ArticleViewCountChangedEventPayload> event) {
        ArticleViewCountChangedEventPayload payload = event.getPayload();
        hotArticleScoreScoreCalculator.saveArticleViewCount(payload.articleId(), payload.count(), payload.changedAt());
    }

    @Override
    public boolean supports(Event<ArticleViewCountChangedEventPayload> event) {
        return EventType.ARTICLE_VIEW_COUNT_CHANGED == event.getType();
    }

}
