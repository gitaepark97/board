package board.backend.comment.application;

import board.backend.common.event.ArticleDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CommentEventHandler {

    private final CommentWriter commentWriter;

    @ApplicationModuleListener
    void handle(ArticleDeletedEvent event) {
        commentWriter.deleteArticle(event.articleId());
    }

}
