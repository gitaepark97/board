package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleLikedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("hotArticleArticleLikedEventHandler")
class ArticleLikedEventHandler implements EventHandler<ArticleLikedEventPayload> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<ArticleLikedEventPayload> event) {
        ArticleLikedEventPayload payload = event.getPayload();
        hotArticleScoreScoreCalculator.saveArticleLikeCount(payload.articleId(), payload.likeCount(), payload.likedAt());
    }

    @Override
    public boolean supports(Event<ArticleLikedEventPayload> event) {
        return EventType.ARTICLE_LIKED == event.getType();
    }

}
