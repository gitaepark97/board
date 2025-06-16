package board.backend.articleRead.web.response;

import board.backend.articleRead.application.dto.ArticleWithWriterAndCounts;
import board.backend.user.web.response.UserResponse;

import java.time.LocalDateTime;

public record ArticleResponse(
    String id,
    String boardId,
    UserResponse writer,
    String title,
    LocalDateTime createdAt,
    Long likeCount,
    Long viewCount,
    Long commentCount
) {

    public static ArticleResponse from(ArticleWithWriterAndCounts articleWithWriterAndCounts) {
        return new ArticleResponse(
            articleWithWriterAndCounts.article().id().toString(),
            articleWithWriterAndCounts.article().boardId().toString(),
            UserResponse.from(articleWithWriterAndCounts.writer()),
            articleWithWriterAndCounts.article().title(),
            articleWithWriterAndCounts.article().createdAt(),
            articleWithWriterAndCounts.likeCount(),
            articleWithWriterAndCounts.viewCount(),
            articleWithWriterAndCounts.commentCount()
        );
    }

}
