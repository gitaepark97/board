package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleLikedEventPaylod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("hotArticleArticleLikedEventHandler")
class ArticleLikedEventHandler implements EventHandler<ArticleLikedEventPaylod> {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;

    @Override
    public void handle(Event<ArticleLikedEventPaylod> event) {
        ArticleLikedEventPaylod payload = event.getPayload();
        hotArticleScoreScoreCalculator.increaseArticleLikeCount(payload.articleId(), payload.likedAt());
    }

    @Override
    public boolean supports(Event<ArticleLikedEventPaylod> event) {
        return EventType.ARTICLE_LIKED == event.getType();
    }

}
