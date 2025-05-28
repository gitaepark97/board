package board.backend.controller.response;

import board.backend.domain.ArticleWithLikeCount;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(
    Long id,
    Long boardId,
    Long writerId,
    String title,
    LocalDateTime createdAt,
    Long likeCount
) {

    public static ArticleSummaryResponse of(ArticleWithLikeCount articleWithLikeCount) {
        return new ArticleSummaryResponse(
            articleWithLikeCount.article().getId(),
            articleWithLikeCount.article().getBoardId(),
            articleWithLikeCount.article().getWriterId(),
            articleWithLikeCount.article().getTitle(),
            articleWithLikeCount.article().getCreatedAt(),
            articleWithLikeCount.likeCount()
        );
    }

}
