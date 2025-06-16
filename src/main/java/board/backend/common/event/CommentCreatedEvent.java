package board.backend.common.event;

import java.time.LocalDateTime;

public record CommentCreatedEvent(
    Long articleId,
    LocalDateTime createdAt
) {

}
