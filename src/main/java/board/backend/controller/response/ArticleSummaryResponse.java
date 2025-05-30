package board.backend.controller.response;

import board.backend.domain.ArticleWithCounts;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(
    Long id,
    Long boardId,
    Long writerId,
    String title,
    LocalDateTime createdAt,
    Long likeCount,
    Long commentCount
) {

    public static ArticleSummaryResponse of(ArticleWithCounts articleWithCounts) {
        return new ArticleSummaryResponse(
            articleWithCounts.article().getId(),
            articleWithCounts.article().getBoardId(),
            articleWithCounts.article().getWriterId(),
            articleWithCounts.article().getTitle(),
            articleWithCounts.article().getCreatedAt(),
            articleWithCounts.likeCount(),
            articleWithCounts.commentCount()
        );
    }

}
