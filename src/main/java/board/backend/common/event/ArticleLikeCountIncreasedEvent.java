package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleLikeCountIncreasedEvent(
    Long articleId,
    LocalDateTime createdAt
) {

}
