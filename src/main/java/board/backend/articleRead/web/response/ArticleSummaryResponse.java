package board.backend.articleRead.web.response;

import board.backend.articleRead.application.dto.ArticleWithWriterAndCounts;
import board.backend.user.web.response.UserSummaryResponse;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(
    String id,
    String boardId,
    UserSummaryResponse writer,
    String title,
    LocalDateTime createdAt,
    Long likeCount,
    Long viewCount,
    Long commentCount
) {

    public static ArticleSummaryResponse from(ArticleWithWriterAndCounts articleWithWriterAndCounts) {
        return new ArticleSummaryResponse(
            articleWithWriterAndCounts.article().id().toString(),
            articleWithWriterAndCounts.article().boardId().toString(),
            UserSummaryResponse.from(articleWithWriterAndCounts.writer()),
            articleWithWriterAndCounts.article().title(),
            articleWithWriterAndCounts.article().createdAt(),
            articleWithWriterAndCounts.likeCount(),
            articleWithWriterAndCounts.viewCount(),
            articleWithWriterAndCounts.commentCount()
        );
    }

}
