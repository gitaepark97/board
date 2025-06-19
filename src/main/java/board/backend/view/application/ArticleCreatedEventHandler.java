package board.backend.view.application;

import board.backend.common.event.Event;
import board.backend.common.event.EventHandler;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("viewArticleCreatedEventHandler")
class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {

    private final ArticleViewCreator articleViewCreator;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();
        articleViewCreator.createCount(payload.articleId());
    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType();
    }

}
