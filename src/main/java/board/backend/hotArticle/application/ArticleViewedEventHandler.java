package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleViewedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("hotArticleArticleViewedEventHandlers")
class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<ArticleViewedEventPayload> event) {
        ArticleViewedEventPayload payload = event.getPayload();
        hotArticleScoreScoreCalculator.saveArticleViewCount(payload.articleId(), payload.viewCount(), payload.viewedAt());
    }

    @Override
    public boolean supports(Event<ArticleViewedEventPayload> event) {
        return EventType.ARTICLE_VIEWED == event.getType();
    }

}
