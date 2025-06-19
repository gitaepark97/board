package board.backend.view.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("viewArticleDeletedEventHandler")
class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

    private final ArticleViewDeleter articleViewDeleter;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        articleViewDeleter.deleteArticle(payload.articleId());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED.equals(event.getType());
    }

}
