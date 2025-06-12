package board.backend.view.application;

import board.backend.common.event.ArticleCreatedEvent;
import board.backend.common.event.ArticleDeletedEvent;
import board.backend.common.event.ArticleReaderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleViewHandler {

    private final ArticleViewWriter articleViewWriter;

    @ApplicationModuleListener
    void handle(ArticleReaderEvent event) {
        articleViewWriter.increaseCount(event.articleId(), event.ip());
    }

    @ApplicationModuleListener
    void handle(ArticleCreatedEvent event) {
        articleViewWriter.saveCount(event.articleId());
    }

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        articleViewWriter.deleteArticle(event.articleId());
    }

}
