package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleViewCountIncreasedEvent(
    Long articleId,
    LocalDateTime createdAt
) {

}
