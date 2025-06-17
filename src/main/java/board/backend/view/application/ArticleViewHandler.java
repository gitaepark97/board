package board.backend.view.application;

import board.backend.common.event.ArticleCreatedEvent;
import board.backend.common.event.ArticleDeletedEvent;
import board.backend.common.event.ArticleReadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleViewHandler {

    private final ArticleViewWriter articleViewWriter;

    @ApplicationModuleListener
    void handle(ArticleCreatedEvent event) {
        articleViewWriter.createCount(event.articleId());
    }

    @Async
    @EventListener
    void handle(ArticleReadEvent event) {
        articleViewWriter.increaseCount(event.articleId(), event.ip());
    }

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        articleViewWriter.deleteArticle(event.articleId());
    }

}
