package board.backend.web.response;

import board.backend.application.dto.ArticleWithWriterAndCounts;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(
    Long id,
    Long boardId,
    UserSummaryResponse writer,
    String title,
    LocalDateTime createdAt,
    Long likeCount,
    Long viewCount,
    Long commentCount
) {

    public static ArticleSummaryResponse from(ArticleWithWriterAndCounts articleWithWriterAndCounts) {
        return new ArticleSummaryResponse(
            articleWithWriterAndCounts.article().getId(),
            articleWithWriterAndCounts.article().getBoardId(),
            UserSummaryResponse.from(articleWithWriterAndCounts.writer()),
            articleWithWriterAndCounts.article().getTitle(),
            articleWithWriterAndCounts.article().getCreatedAt(),
            articleWithWriterAndCounts.likeCount(),
            articleWithWriterAndCounts.viewCount(),
            articleWithWriterAndCounts.commentCount()
        );
    }

}
