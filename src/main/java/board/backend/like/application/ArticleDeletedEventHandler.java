package board.backend.like.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("likeArticleDeletedEventHandler")
class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

    private final ArticleLikeDeleter articleLikeDeleter;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        articleLikeDeleter.deleteArticle(payload.articleId());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED.equals(event.getType());
    }

}
