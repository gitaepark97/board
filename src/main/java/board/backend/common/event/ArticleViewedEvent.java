package board.backend.common.event;

import java.time.LocalDateTime;

public record ArticleViewedEvent(
    Long articleId,
    LocalDateTime viewedAt
) {

}
