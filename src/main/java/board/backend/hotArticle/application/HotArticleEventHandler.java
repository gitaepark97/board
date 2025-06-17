package board.backend.hotArticle.application;

import board.backend.common.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class HotArticleEventHandler {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;
    private final HotArticleWriter hotArticleWriter;

    @Async
    @EventListener
    void handle(ArticleLikeCountIncreasedEvent event) {
        hotArticleScoreScoreCalculator.increaseArticleLikeCount(event.articleId(), event.createdAt());
    }

    @Async
    @EventListener
    void handle(ArticleLikeCountDecreasedEvent event) {
        hotArticleScoreScoreCalculator.decreaseArticleLikeCount(event.articleId(), event.unlikedAt());
    }

    @Async
    @EventListener
    void handle(ArticleViewCountIncreasedEvent event) {
        hotArticleScoreScoreCalculator.increaseArticleViewCount(event.articleId(), event.viewCount(), event.createdAt());
    }

    @Async
    @EventListener
    void handle(ArticleCommentCountIncreasedEvent event) {
        hotArticleScoreScoreCalculator.increaseArticleCommentCount(event.articleId(), event.createdAt());
    }

    @Async
    @EventListener
    void handle(ArticleCommentDecreasedEvent event) {
        hotArticleScoreScoreCalculator.decreaseArticleCommentCount(event.articleId(), event.createdAt());
    }

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        hotArticleWriter.delete(event.articleId());
    }


}
