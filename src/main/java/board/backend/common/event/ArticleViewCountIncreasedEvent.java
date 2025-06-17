package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleViewCountIncreasedEvent(
    Long articleId,
    Long viewCount,
    LocalDateTime createdAt
) {

}
