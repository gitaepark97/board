package board.backend.hotArticle.application;

import board.backend.common.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class HotArticleEventHandler {

    private final HotArticleScoreScoreCalculator hotArticleScoreScoreCalculator;
    private final HotArticleWriter hotArticleWriter;

    @ApplicationModuleListener
    void handle(ArticleLikeCountIncreasedEvent event) {
        hotArticleScoreScoreCalculator.increaseArticleLikeCount(event.articleId(), event.createdAt());
    }

    @ApplicationModuleListener
    void handle(ArticleLikeCountDecreasedEvent event) {
        hotArticleScoreScoreCalculator.decreaseArticleLikeCount(event.articleId(), event.unlikedAt());
    }

    @ApplicationModuleListener
    void handle(ArticleViewCountIncreasedEvent event) {
        hotArticleScoreScoreCalculator.increaseArticleViewCount(event.articleId(), event.createdAt());
    }

    @ApplicationModuleListener
    void handle(ArticleCommentCountIncreasedEvent event) {
        hotArticleScoreScoreCalculator.increaseArticleCommentCount(event.articleId(), event.createdAt());
    }

    @ApplicationModuleListener
    void handle(ArticleCommentDecreasedEvent event) {
        hotArticleScoreScoreCalculator.decreaseArticleCommentCount(event.articleId(), event.createdAt());
    }

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        hotArticleWriter.delete(event.articleId());
    }


}
