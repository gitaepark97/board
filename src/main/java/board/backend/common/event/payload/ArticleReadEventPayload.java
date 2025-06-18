package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

public record ArticleReadEventPayload(
    Long articleId,
    String ip
) implements EventPayload {

}
