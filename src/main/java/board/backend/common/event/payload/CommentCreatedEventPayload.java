package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record CommentCreatedEventPayload(
    Long articleId,
    LocalDateTime createdAt
) implements EventPayload {

}
