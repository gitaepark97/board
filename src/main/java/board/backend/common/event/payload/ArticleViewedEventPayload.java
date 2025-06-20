package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record ArticleViewedEventPayload(
    Long articleId,
    Long increment,
    LocalDateTime createdAt
) implements EventPayload {

}
