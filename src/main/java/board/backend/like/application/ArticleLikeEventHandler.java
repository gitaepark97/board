package board.backend.like.application;

import board.backend.common.event.ArticleDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeEventHandler {

    private final ArticleLikeWriter articleLikeWriter;

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        articleLikeWriter.deleteArticle(event.articleId());
    }

}
