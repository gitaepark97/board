package board.backend.comment.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("commentArticleDeletedEventHandler")
class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

    private final CommentWriter commentWriter;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        commentWriter.deleteArticle(payload.articleId());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED.equals(event.getType());
    }

}
