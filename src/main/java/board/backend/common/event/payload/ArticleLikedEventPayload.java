package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record ArticleLikedEventPayload(
    Long articleId,
    Long likeCount,
    LocalDateTime likedAt
) implements EventPayload {

}
