package board.backend.common.event;

import java.time.LocalDateTime;

public record CommentDeletedEvent(
    Long articleId,
    LocalDateTime deletedAt
) {

}
