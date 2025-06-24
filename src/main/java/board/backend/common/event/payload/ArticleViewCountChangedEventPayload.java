package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record ArticleViewCountChangedEventPayload(
    Long articleId,
    Long count,
    LocalDateTime changedAt
) implements EventPayload {

}
