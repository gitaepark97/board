package board.backend.articleRead.web.response;

import board.backend.articleRead.application.dto.ArticleDetail;
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

    public static ArticleResponse from(ArticleDetail articleDetail) {
        return new ArticleResponse(
            articleDetail.article().id().toString(),
            articleDetail.article().boardId().toString(),
            UserResponse.from(articleDetail.writer()),
            articleDetail.article().title(),
            articleDetail.article().createdAt(),
            articleDetail.likeCount(),
            articleDetail.viewCount(),
            articleDetail.commentCount()
        );
    }

}
