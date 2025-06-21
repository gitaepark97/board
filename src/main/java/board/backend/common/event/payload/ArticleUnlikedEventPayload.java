package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record ArticleUnlikedEventPayload(
    Long articleId,
    Long likeCount,
    LocalDateTime unlikedAt
) implements EventPayload {

}
