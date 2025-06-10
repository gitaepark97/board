package board.backend.comment.web.request;

import board.backend.common.web.request.NumericString;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
    @NotBlank
    @NumericString
    String articleId,

    @NumericString
    String parentCommentId,

    @NotBlank
    String content
) {

    public Long articleIdAsLong() {
        return Long.parseLong(articleId);
    }

    public Long parentCommentIdAsLong() {
        return parentCommentId == null ? null : Long.parseLong(parentCommentId);
    }

}

