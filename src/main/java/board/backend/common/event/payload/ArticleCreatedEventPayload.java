package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

public record ArticleCreatedEventPayload(
    Long articleId
) implements EventPayload {

}
