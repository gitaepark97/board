package board.backend.comment.application;

import board.backend.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentEventHandler {

    private final CommentWriter commentWriter;

    @ApplicationModuleListener
    void handle(ArticleDeletedEventPayload event) {
        commentWriter.deleteArticle(event.articleId());
    }

}
