package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleCommentDecreasedEvent(
    Long articleId,
    LocalDateTime createdAt
) {

}
