package example.hugo.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.hugo.domain.ArticleLike;

import java.time.LocalDateTime;

public record ArticleLikeResponse(
    Long articleLikeId,
    Long articleId,
    Long userId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

    public static ArticleLikeResponse from(ArticleLike articleLike) {
        return new ArticleLikeResponse(articleLike.articleLikeId(), articleLike.articleId(), articleLike.userId(), articleLike.createdAt());
    }

}
