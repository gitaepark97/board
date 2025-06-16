package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleLikeCountDecreasedEvent(
    Long articleId,
    LocalDateTime unlikedAt
) {

}
