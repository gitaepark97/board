package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record ArticleViewedEventPayload(
    Long articleId,
    Long viewCount,
    LocalDateTime viewedAt
) implements EventPayload {

}
