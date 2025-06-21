package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record CommentCreatedEventPayload(
    Long articleId,
    Long commentCount,
    LocalDateTime createdAt
) implements EventPayload {

}
