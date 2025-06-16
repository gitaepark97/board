package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleCommentCountIncreasedEvent(
    Long articleId,
    LocalDateTime createdAt
) {

}
