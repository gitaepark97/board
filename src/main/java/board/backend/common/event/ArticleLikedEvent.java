package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleLikedEvent(
    Long articleId,
    LocalDateTime likedAt
) {

}
