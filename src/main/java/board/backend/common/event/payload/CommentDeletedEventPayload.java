package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record CommentDeletedEventPayload(
    Long articleId,
    LocalDateTime deletedAt
) implements EventPayload {

}
