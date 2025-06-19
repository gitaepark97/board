package board.backend.hotArticle.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("hotArticleArticleDeletedEventHandler")
class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

    private final HotArticleDeleter hotArticleDeleter;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        hotArticleDeleter.delete(payload.articleId());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }

}
