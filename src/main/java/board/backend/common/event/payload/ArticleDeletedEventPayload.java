package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

public record ArticleDeletedEventPayload(
    Long articleId
) implements EventPayload {

}
