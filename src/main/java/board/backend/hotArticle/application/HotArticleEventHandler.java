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
    void handle(ArticleLikedEvent event) {
        hotArticleScoreScoreCalculator.afterArticleLiked(event.articleId(), event.likedAt());
    }

    @ApplicationModuleListener
    void handle(ArticleUnlikedEvent event) {
        hotArticleScoreScoreCalculator.afterArticleUnliked(event.articleId(), event.unlikedAt());
    }

    @ApplicationModuleListener
    void handle(ArticleViewedEvent event) {
        hotArticleScoreScoreCalculator.afterArticleViewed(event.articleId(), event.viewedAt());
    }

    @ApplicationModuleListener
    void handle(CommentCreatedEvent event) {
        hotArticleScoreScoreCalculator.afterCommentCreated(event.articleId(), event.createdAt());
    }

    @ApplicationModuleListener
    void handle(CommentDeletedEvent event) {
        hotArticleScoreScoreCalculator.afterCommentDeleted(event.articleId(), event.deletedAt());
    }

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        hotArticleWriter.delete(event.articleId());
    }


}
