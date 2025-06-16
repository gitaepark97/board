package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleUnlikedEvent(
    Long articleId,
    LocalDateTime unlikedAt
) {

}
