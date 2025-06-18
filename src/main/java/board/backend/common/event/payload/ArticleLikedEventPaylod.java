package board.backend.common.event.payload;

import board.backend.common.event.EventPayload;

import java.time.LocalDateTime;

public record ArticleLikedEventPaylod(
    Long articleId,
    LocalDateTime likedAt
) implements EventPayload {

}
