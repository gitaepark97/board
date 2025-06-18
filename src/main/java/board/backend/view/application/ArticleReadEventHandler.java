package board.backend.view.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleReadEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("viewArticleReadEventHandler")
class ArticleReadEventHandler implements EventHandler<ArticleReadEventPayload> {

    private final ArticleViewWriter articleViewWriter;

    @Override
    public void handle(Event<ArticleReadEventPayload> event) {
        ArticleReadEventPayload payload = event.getPayload();
        articleViewWriter.increaseCount(payload.articleId(), payload.ip());
    }

    @Override
    public boolean supports(Event<ArticleReadEventPayload> event) {
        return EventType.ARTICLE_READ.equals(event.getType());
    }

}
